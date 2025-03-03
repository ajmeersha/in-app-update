import { EventSubscription } from "expo-modules-core";

import ExpoInappUpdateModule from "./ExpoInappUpdateModule";

export function updateCancelled(listener: (event) => void): EventSubscription {
  return ExpoInappUpdateModule.addListener("updateCancelled", listener);
}

export function immediateUpdateCancelled(
  listener: (event) => void
): EventSubscription {
  return ExpoInappUpdateModule.addListener(
    "immediateUpdateCancelled",
    listener
  );
}

export async function isUpdateAvailable(): Promise<boolean> {
  return await ExpoInappUpdateModule.isUpdateAvailable();
}

export function startUpdate(): void {
  return ExpoInappUpdateModule.startUpdate();
}
