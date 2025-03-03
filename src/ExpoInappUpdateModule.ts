import { NativeModule, requireNativeModule } from "expo";

import { ExpoInappUpdateModuleEvents } from "./ExpoInappUpdate.types";

declare class ExpoInappUpdateModule extends NativeModule<ExpoInappUpdateModuleEvents> {
  immediateUpdateCancelled(listener: (event: any) => void);
  updateCancelled(listener: (event: any) => void);
  isUpdateAvailable(): Promise<boolean>;
  startUpdate();
}

// This call loads the native module object from the JSI.
export default requireNativeModule<ExpoInappUpdateModule>("ExpoInappUpdate");
