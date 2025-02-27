// Reexport the native module. On web, it will be resolved to ExpoInappUpdateModule.web.ts
// and on native platforms to ExpoInappUpdateModule.ts
export { default } from './ExpoInappUpdateModule';
export { default as ExpoInappUpdateView } from './ExpoInappUpdateView';
export * from  './ExpoInappUpdate.types';
