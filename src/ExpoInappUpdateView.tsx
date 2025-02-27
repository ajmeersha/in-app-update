import { requireNativeView } from 'expo';
import * as React from 'react';

import { ExpoInappUpdateViewProps } from './ExpoInappUpdate.types';

const NativeView: React.ComponentType<ExpoInappUpdateViewProps> =
  requireNativeView('ExpoInappUpdate');

export default function ExpoInappUpdateView(props: ExpoInappUpdateViewProps) {
  return <NativeView {...props} />;
}
