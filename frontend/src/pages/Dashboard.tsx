import {Button, Container, Group, Paper, Stack, Text, ThemeIcon, Title} from '@mantine/core';
import { IconBarbell } from '@tabler/icons-react';

function Dashboard() {
  return (
    <Container size="lg" py="xl">
      <Stack gap="xl">
        <Text c="dimmed" size="lg">
          Build your next workout
        </Text>

        <Group justify="space-between">
          <Title order={2}>
            My Workout Plans
          </Title>
        </Group>

        <Paper withBorder shadow="md" p="xl" radius="md">
          <Stack align="center" gap="sm">          
            <ThemeIcon size={64} radius="xl" variant="light" color="violet">
              <IconBarbell size={30} />
            </ThemeIcon>

            <Text fw={700} size="lg">
              No workout plans yet
            </Text>

            <Text c="dimmed" ta="center">
              Create your first workout plan to get started
            </Text>

            <Button color="pink">
              Create Plan
            </Button>
          </Stack>
        </Paper>
      </Stack>
    </Container>
  );
}
export default Dashboard;