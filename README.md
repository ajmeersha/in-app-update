# expo-inapp-update

android in-app update module for expo with handling cancel event

## Installation

Use the package manager npm or yarn to install expo-inapp-update

```bash
npm install expo-inapp-update
```

or

```bash
yarn add expo-inapp-update
```

## Usage

```python
import * as ExpoInappUpdate from "expo-inapp-update";

// Check for update available
const isUpdateAvailable = await ExpoInappUpdate.isUpdateAvailable();

// start the inapp update flow based on the update available value
 if (isUpdateAvailable) {
      ExpoInappUpdate.startUpdate();
    }

// handle the cancel or accept event based on the event listeners
 useEffect(() => {
    const ImmediateUpdatesubscription =
      ExpoInappUpdate.immediateUpdateCancelled(({ resultCode }) => {
        // handle the immediate update cancel event or success event based on the result code
        console.log(resultCode, "result");
      });
    const Flexiblesubscription = ExpoInappUpdate.updateCancelled(
      ({ resultCode }) => {
        // handle the flexible update cancel event or success event based on the result code
        console.log(resultCode, "flexible result");
      }
    );

    return () => {
      Flexiblesubscription.remove();
      ImmediateUpdatesubscription.remove();
    };
  }, []);

```

## Contributing

Pull requests are welcome. For major changes, please open an issue first
to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

[MIT](https://choosealicense.com/licenses/mit/)
