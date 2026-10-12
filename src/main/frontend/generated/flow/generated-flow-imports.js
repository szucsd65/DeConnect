import '@vaadin/polymer-legacy-adapter/style-modules.js';
import '@vaadin/vertical-layout/theme/lumo/vaadin-vertical-layout.js';
import '@vaadin/login/theme/lumo/vaadin-login-form.js';
import '@vaadin/button/theme/lumo/vaadin-button.js';
import '@vaadin/tooltip/theme/lumo/vaadin-tooltip.js';
import 'Frontend/generated/jar-resources/disableOnClickFunctions.js';
import '@vaadin/common-frontend/ConnectionIndicator.js';
import '@vaadin/vaadin-lumo-styles/color-global.js';
import '@vaadin/vaadin-lumo-styles/typography-global.js';
import '@vaadin/vaadin-lumo-styles/sizing.js';
import '@vaadin/vaadin-lumo-styles/spacing.js';
import '@vaadin/vaadin-lumo-styles/style.js';
import '@vaadin/vaadin-lumo-styles/vaadin-iconset.js';
import 'Frontend/generated/jar-resources/ReactRouterOutletElement.tsx';

const loadOnDemand = (key) => {
  const pending = [];
  if (key === '721e6928f755b458b9e1ca85d5003f3b1cdecb91fa43effccb97c389bf843f50') {
    pending.push(import('./chunks/chunk-59e97763c0ef10fe8c9c5130e10befa06b9b6d20e9f5c6e2ec4688f223fe30de.js'));
  }
  if (key === 'b1c18549f73ccbb903d799c91776131cce83ac6fe47b1cd5c7868150f3ddadbf') {
    pending.push(import('./chunks/chunk-97f85ca367195e355634d5e5000bd8e52e8eb6f45b6f6cd1f3ceaaf9d1d9eb44.js'));
  }
  if (key === '91b9f56be8bf1fc15e5cd5a7ba9343cc4ac5e853e4a43db884f0b420e8045b8c') {
    pending.push(import('./chunks/chunk-4929589ad0505ba7f930dd39223fcd95a48f9bcb8f5510f10383d6cc4374edee.js'));
  }
  if (key === 'b3e85cd208d61481eb1fe3762d6e0c8eff1fcb8e1acea8d383e11c8e37c792ba') {
    pending.push(import('./chunks/chunk-85fbc639a0673686c306e0fb980f61c4089998d18e5f46e112efb988443ea6d9.js'));
  }
  if (key === 'e5468199f64b0c0473ac3df9751b13c12ebcf5d6f38a839d296c9044f9fd73af') {
    pending.push(import('./chunks/chunk-87efcebd96062275b3fc1f1bcbf763b99b6b54db21a812f4c7f63fe6ad9f6d0e.js'));
  }
  if (key === '62486f28d7517c2cd87d7a0dea7d51a2a8ff7134d3c9670f8c426f4332f31ba1') {
    pending.push(import('./chunks/chunk-87efcebd96062275b3fc1f1bcbf763b99b6b54db21a812f4c7f63fe6ad9f6d0e.js'));
  }
  return Promise.all(pending);
}

window.Vaadin = window.Vaadin || {};
window.Vaadin.Flow = window.Vaadin.Flow || {};
window.Vaadin.Flow.loadOnDemand = loadOnDemand;
window.Vaadin.Flow.resetFocus = () => {
 let ae=document.activeElement;
 while(ae&&ae.shadowRoot) ae = ae.shadowRoot.activeElement;
 return !ae || ae.blur() || ae.focus() || true;
}