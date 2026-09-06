import {
  Button,
  Container,
  SimpleGrid,
  Paper,
  Stack,
  Text,
  Title,
  Group,
  ThemeIcon,
  ActionIcon
} from "@mantine/core";
import { IconBarbell, IconPlus } from '@tabler/icons-react';
import { useEffect, useState } from "react";
import { api } from "../services/api";
import type { WorkoutPlan } from "../types/WorkoutPlan";
import WorkoutPlanCard from "../components/WorkoutPlanCard";
import './Dashboard.css';

type RequestState<T> =
  | { status: "idle" }
  | { status: "loading" }
  | { status: "success"; data: T }
  | { status: "error"; error: Error };

function Dashboard() {
  const [state, setState] = useState<RequestState<WorkoutPlan[]>>({
    status: "loading",
  });
 // Load workout plans when the Dashboard first appears
  useEffect(() => {
    api
      .getWorkoutPlans()
      .then((workoutPlans) => {
        setState({ status: "success", data: workoutPlans });
      })
      .catch((error: Error) => {
        setState({ status: "error", error });
      });
  }, []);

  // Function renders loading, error, no workout plan card, or workout plan cards
  function renderWorkoutPlans() {
    if (state.status === "loading") {
      return <Text c="dimmed">Loading workout plans...</Text>;
    }

    if (state.status === "error") {
      return <Text c="red">{state.error.message}</Text>;
    }

    if (state.status === "success") {
      if (state.data.length === 0) {
        return (
          <Paper withBorder shadow="md" p="xl" radius="md">
            <Stack align="center" gap="sm">
              <ThemeIcon size={64} radius="xl" variant="light" color="pink">
                <IconBarbell size={30} color="var(--mantine-color-pink-6)" />
              </ThemeIcon>
              <Text fw={700} size="lg">
                No workout plans yet
              </Text>
              <Text c="dimmed" ta="center">
                Create your first workout plan to get started
              </Text>
              <Button color="pink">Create Plan</Button>
            </Stack>
          </Paper>
        );
      }
      return (
        <>
          <SimpleGrid cols={{ base: 1, sm: 2, lg: 3 }}>
            {state.data.map((workoutPlan) => (
              <WorkoutPlanCard key={workoutPlan.id} workoutPlan={workoutPlan} />
            ))}
          </SimpleGrid>
        </>
      );
    }
    return null;
  }

  return (
    <Container size="lg" py="xl">
      <Stack gap="xl">
        <Text c="dimmed" size="lg">
          Build your next workout
        </Text>
        <Group justify="space-between">
          <Title order={2}>My Workout Plans</Title>
          {state.status === "success" && state.data.length > 0 ? (
            <>
              <Button color="pink" className="createPlanDesktop">
                Create Plan
              </Button>            
              <ActionIcon color="pink" size="lg" className="createPlanMobile" aria-label="Create plan">
                <IconPlus size={20} />
              </ActionIcon>
            </>
          ) : null}
        </Group>
        {renderWorkoutPlans()}
      </Stack>
    </Container>
  );
}

export default Dashboard;
