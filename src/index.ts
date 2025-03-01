// Reexport the native module. On web, it will be resolved to ExpoInappUpdateModule.web.ts
// and on native platforms to ExpoInappUpdateModule.ts
import { EventSubscription } from "expo-modules-core";
import ExpoInappUpdateModule from "./ExpoInappUpdateModule";

export type ThemeChangeEvent = {
  theme: string;
};

export function addThemeChangeListener(
  listener: (event: ThemeChangeEvent) => void
): EventSubscription {
  return ExpoInappUpdateModule.addListener("onChange", listener);
}

export function getTheme(): string {
  return ExpoInappUpdateModule.getTheme();
}

export async function chechUpdateAvailable(): Promise<boolean> {
  return await ExpoInappUpdateModule.isUpdateAvailable();
}

export function checkForUpdate(): void {
  return ExpoInappUpdateModule.checkForUpdate();
}

export function setTheme(theme: string): void {
  return ExpoInappUpdateModule.setTheme(theme);
}
