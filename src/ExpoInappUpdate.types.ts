import type { StyleProp, ViewStyle } from "react-native";

export type OnLoadEventPayload = {
  url: string;
};

export type ExpoInappUpdateModuleEvents = {
  immediateUpdateCancelled: (listener: (event: any) => void) => void;
  updateCancelled: (listener: (event: any) => void) => void;
};

export type ExpoInappUpdateViewProps = {
  url: string;
  onLoad: (event: { nativeEvent: OnLoadEventPayload }) => void;
  style?: StyleProp<ViewStyle>;
};
