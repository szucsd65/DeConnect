package com.example.deconnect.views;

import com.example.deconnect.model.Event;
import com.example.deconnect.model.Material;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.streams.DownloadEvent;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MaterialTemplate extends VerticalLayout {
    private final VerticalLayout template = new VerticalLayout();
    private final List<Material> materialItems = new ArrayList<>();

    public MaterialTemplate() {
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
        H4 materialName = new H4(material.getFileName());

        Span time = new Span(material.getPostedAt().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        Paragraph message = new Paragraph(material.getMessage());

        materialPost.add(user,materialName,  time, message);
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
            Anchor downloadBtn = new Anchor(downloadHandler, "Letöltés");
            downloadBtn.getElement().getThemeList().add("button");
            downloadBtn.addClassNames("materialDownloadBtn");
            materialPost.add(downloadBtn);
        }
        template.add(materialPost);
    }

    public void clearContent() {
        materialItems.clear();
        template.removeAll();
    }
}
