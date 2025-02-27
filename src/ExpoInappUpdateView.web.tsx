import * as React from 'react';

import { ExpoInappUpdateViewProps } from './ExpoInappUpdate.types';

export default function ExpoInappUpdateView(props: ExpoInappUpdateViewProps) {
  return (
    <div>
      <iframe
        style={{ flex: 1 }}
        src={props.url}
        onLoad={() => props.onLoad({ nativeEvent: { url: props.url } })}
      />
    </div>
  );
}
