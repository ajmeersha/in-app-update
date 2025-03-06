export type ExpoInappUpdateModuleEvents = {
  immediateUpdateCancelled: (listener: (event: any) => void) => void;
  updateCancelled: (listener: (event: any) => void) => void;
};
