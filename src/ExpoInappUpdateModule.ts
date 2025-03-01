import { NativeModule, requireNativeModule } from "expo";

import { ExpoInappUpdateModuleEvents } from "./ExpoInappUpdate.types";

declare class ExpoInappUpdateModule extends NativeModule<ExpoInappUpdateModuleEvents> {
  // PI: number;
  // hello(): string;
  getTheme();
  setTheme(theme: string);
}

// This call loads the native module object from the JSI.
export default requireNativeModule<ExpoInappUpdateModule>("ExpoInappUpdate");
