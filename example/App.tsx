import * as ExpoInappUpdate from "expo-inapp-update";
import { useEffect } from "react";
import { Button, SafeAreaView, ScrollView, Text, View } from "react-native";

export default function App() {
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

  const handleUpdateAvailable = async () => {
    const isUpdateAvailable = await ExpoInappUpdate.isUpdateAvailable();
    if (isUpdateAvailable) {
      ExpoInappUpdate.startUpdate();
    }
    console.log(isUpdateAvailable, "isupdate");
  };
  return (
    <SafeAreaView style={styles.container}>
      <ScrollView style={styles.container}>
        <Text style={styles.header}>Inapp update API Example</Text>
        <Group name="Functions">
          <Button
            title="Check update from google play console"
            onPress={() => handleUpdateAvailable()}
          />
        </Group>
      </ScrollView>
    </SafeAreaView>
  );
}

function Group(props: { name: string; children: React.ReactNode }) {
  return (
    <View style={styles.group}>
      <Text style={styles.groupHeader}>{props.name}</Text>
      {props.children}
    </View>
  );
}

const styles = {
  header: {
    fontSize: 30,
    margin: 20,
  },
  groupHeader: {
    fontSize: 20,
    marginBottom: 20,
  },
  group: {
    margin: 20,
    backgroundColor: "#fff",
    borderRadius: 10,
    padding: 20,
  },
  container: {
    flex: 1,
    backgroundColor: "#eee",
  },
  view: {
    flex: 1,
    height: 200,
  },
};
