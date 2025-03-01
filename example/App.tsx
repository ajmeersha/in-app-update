import { useEvent } from "expo";
import * as ExpoInappUpdate from "expo-inapp-update";
import { useEffect, useState } from "react";
import { Button, SafeAreaView, ScrollView, Text, View } from "react-native";

export default function App() {
  // const onChangePayload = useEvent(ExpoInappUpdate, 'onChange');

  const [theme, setTheme] = useState<string>(ExpoInappUpdate.getTheme());

  useEffect(() => {
    const subscription = ExpoInappUpdate.addThemeChangeListener(
      ({ theme: newTheme }) => {
        setTheme(newTheme);
      }
    );

    return () => subscription.remove();
  }, [setTheme]);

  const handleUpdateAvailable = async () => {
    const isUpdateAvailable = await ExpoInappUpdate.chechUpdateAvailable();
    console.log(isUpdateAvailable);
  };

  // Toggle between dark and light theme
  const nextTheme = theme === "dark" ? "light" : "dark";
  return (
    <SafeAreaView style={styles.container}>
      <ScrollView style={styles.container}>
        <Text style={styles.header}>Module API Example</Text>
        {/* <Group name="Constants">
          <Text>{ExpoInappUpdate.PI}</Text>
        </Group> */}
        <Group name="Functions">
          <Text>{ExpoInappUpdate.getTheme()}</Text>

          <Button
            title={`Set theme to ${nextTheme}`}
            // onPress={() => ExpoInappUpdate.setTheme(nextTheme)}
            onPress={() => handleUpdateAvailable()}
          />
        </Group>
        {/* <Group name="Async functions">
          <Button
            title="Set value"
            onPress={async () => {
              await ExpoInappUpdate.setValueAsync("Hello from JS!");
            }}
          />
        </Group> */}
        {/* <Group name="Events">
          <Text>{onChangePayload?.value}</Text>
        </Group> */}
        {/* <Group name="Views">
          <ExpoInappUpdateView
            url="https://www.example.com"
            onLoad={({ nativeEvent: { url } }) => console.log(`Loaded: ${url}`)}
            style={styles.view}
          />
        </Group> */}
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
