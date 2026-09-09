import { useEffect, useState } from "react";
import { useNavigate } from "react-router";

import {
  ActionIcon,
  Button,
  Container,
  Group,
  Paper,
  SimpleGrid,
  Stack,
  Text,
  ThemeIcon,
  Title,
} from "@mantine/core";
import { IconBarbell, IconPlus } from "@tabler/icons-react";
import WorkoutPlanCard from "../components/WorkoutPlanCard";
import { api } from "../services/api";
import type { WorkoutPlan } from "../types/WorkoutPlan";
import type { RequestState } from '../types/RequestState';
import "../styles/Dashboard.css";

function Dashboard() {
  const [state, setState] = useState<RequestState<WorkoutPlan[]>>({
    status: "loading",
  });
  const [exerciseCounts, setExerciseCounts] = useState<Record<number, number>>({});
  const navigate = useNavigate();

  // Load workout plans when the Dashboard first appears
  useEffect(() => {
    api
      .getWorkoutPlans()
      .then(async (workoutPlans) => {
        setState({ status: "success", data: workoutPlans });
          const counts = await Promise.all(
            workoutPlans.map(async (workoutPlan) => {
              const exercises = await api.getWorkoutPlanExercises(workoutPlan.id);
              return [workoutPlan.id, exercises.length] as const;
            }),
          );
          setExerciseCounts(Object.fromEntries(counts));
        })
      .catch((error: Error) => {
        console.error(error)
        setState({ status: "error", error });
      });
  }, []);

  // Function renders loading, error, no workout plan card, or workout plan cards
  function renderWorkoutPlans() {
    if (state.status === "loading") {
      return <Text c="dimmed">Loading workout plans...</Text>;
    }

    if (state.status === "error") {
      return <Text c="red">Unable to load workout plans. Please try again.</Text>;
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

              <Button
                color="pink"
                onClick={() => navigate("/workout-plans/new")}>
                Create Plan
              </Button>

            </Stack>
          </Paper>
        );
      }
      return (
        <SimpleGrid cols={{ base: 1, sm: 2, lg: 3 }}>
          {state.data.map((workoutPlan) => (
            <WorkoutPlanCard
              key={workoutPlan.id}
              workoutPlan={workoutPlan}
              exerciseCount={exerciseCounts[workoutPlan.id] ?? 0}
            />
          ))}
        </SimpleGrid>
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
              <Button
                color="pink"
                className="createPlanDesktop"
                onClick={() => navigate("/workout-plans/new")}>
                Create Plan
              </Button>
              
              <ActionIcon
                color="pink"
                size="lg"
                className="createPlanMobile"
                aria-label="Create plan"
                onClick={() => navigate("/workout-plans/new")}>
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
