import { registerWebModule, NativeModule } from 'expo';

import { ExpoInappUpdateModuleEvents } from './ExpoInappUpdate.types';

class ExpoInappUpdateModule extends NativeModule<ExpoInappUpdateModuleEvents> {
  PI = Math.PI;
  async setValueAsync(value: string): Promise<void> {
    this.emit('onChange', { value });
  }
  hello() {
    return 'Hello world! 👋';
  }
}

export default registerWebModule(ExpoInappUpdateModule);
