package com.example.deconnect.views;

import com.example.deconnect.model.Material;
import com.example.deconnect.model.UserInfo;
import com.example.deconnect.service.MaterialService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.server.streams.*;

import java.io.ByteArrayInputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class MaterialTemplate extends VerticalLayout {
    private final VerticalLayout template = new VerticalLayout();
    private final List<Material> materialItems = new ArrayList<>();
    private UserInfo currentUser;
    private MaterialService materialService;
    private final Runnable refreshCallback;
    private Runnable onDelete;

    public MaterialTemplate(UserInfo currentUser, MaterialService materialService, Runnable refreshCallback) {
        this.currentUser = currentUser;
        this.materialService = materialService;
        this.refreshCallback = refreshCallback;

        setSizeFull();
        setPadding(false);
        setSpacing(true);

        template.setWidthFull();
        template.setPadding(false);
        template.setSpacing(true);
        template.addClassNames("postTemplate");

        add(new H4("Tananyagok"), template);
    }

    public void addMaterial(Material material) {
        materialItems.add(material);

        Div materialPost = new Div();
        materialPost.addClassNames("posts");
        H4 user = new H4(material.getUsername());
        if (material.getUsername().equals(currentUser.getUsername())) {
            Button deleteBtn = new Button(VaadinIcon.CLOSE.create(), e -> {
                materialService.deleteMaterial(material);
                if (onDelete != null) {
                    onDelete.run();
                }
            });
            Button updateBtn = new Button(VaadinIcon.REFRESH.create(), e -> {
                Dialog materialTextWindow = new Dialog();
                materialTextWindow.open();
                AtomicReference<String> uploadedFileName = new AtomicReference<>();
                AtomicReference<String> uploadedContentType = new AtomicReference<>();
                AtomicReference<byte[]> uploadedData = new AtomicReference<>();
                VerticalLayout content = new VerticalLayout();
                content.setWidthFull();
                content.setAlignItems(FlexComponent.Alignment.CENTER);
                materialTextWindow.setHeaderTitle("Új Tananyag");

                TextArea materialTextInput = new TextArea("Leírás:");
                materialTextInput.addClassNames("textBox");
                InMemoryUploadHandler inMemoryHandler = UploadHandler
                        .inMemory((metadata, data) -> {
                            uploadedFileName.set( metadata.fileName());
                            uploadedContentType.set(metadata.contentType());
                            uploadedData.set(data);
                        });
                Upload upload = new Upload(inMemoryHandler);
                upload.setWidthFull();
                upload.setMaxFiles(1);
                Button materialSave = new Button("Küldés", saveEvent -> {
                    if (uploadedData.get() != null) {
                        material.setFileName(uploadedFileName.get());
                        material.setMime(uploadedContentType.get());
                        material.setData(uploadedData.get());
                    }

                    material.setMessage(materialTextInput.getValue());
                    material.setData(uploadedData.get());

                    materialService.updateMaterial(material);
                    materialTextInput.clear();
                    materialTextWindow.close();

                    if (refreshCallback != null) {
                        refreshCallback.run();
                    }
                });

                content.add(materialTextInput);
                content.add(upload);
                materialTextWindow.add(content);

                Button materialCancel = new Button("Mégse", cancelEvent -> materialTextWindow.close());
                materialSave.addClassNames("footerBtns");
                materialCancel.addClassNames("footerBtns");
                materialTextWindow.getFooter().add(materialCancel, materialSave);
            });
            updateBtn.addClassNames("crudUpdate");
            deleteBtn.addClassNames("crudDelete");
            HorizontalLayout crudButtons = new HorizontalLayout(updateBtn, deleteBtn);
            crudButtons.addClassNames("crudBtnsLine");
            materialPost.add(crudButtons);
        }
        H4 materialName = new H4(material.getFileName());

        Span time = new Span(material.getPostedAt().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        Paragraph message = new Paragraph(material.getMessage());

        materialPost.add(materialName, user, time, message);
        if (material.getData() != null) {
            DownloadHandler downloadHandler = DownloadHandler.fromInputStream((e) -> {
                try {
                    return new DownloadResponse(
                            new ByteArrayInputStream(material.getData()),
                            material.getFileName(),
                            material.getMime(),
                            material.getData().length);
                } catch (Exception k) {
                    return DownloadResponse.error(500);
                }
            });
            Anchor downloadBtn = new Anchor(downloadHandler, "");
            downloadBtn.getElement().getThemeList().add("button");
            downloadBtn.addClassNames("materialDownloadBtn");
            downloadBtn.add(VaadinIcon.DOWNLOAD.create());
            materialPost.add(downloadBtn);
        }
        template.add(materialPost);
    }

    public void clearContent() {
        materialItems.clear();
        template.removeAll();
    }

    public void setOnDelete(Runnable onDelete) {
        this.onDelete = onDelete;
    }
}
