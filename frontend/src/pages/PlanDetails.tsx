import { useEffect, useState } from "react";
import { useNavigate, useParams, Link } from "react-router";
import {
  Container,
  Paper,
  Stack,
  Text,
  Title,
  Image,
  Group,
  Anchor,
} from "@mantine/core";
import { IconArrowLeft } from "@tabler/icons-react";
import { api } from "../services/api";
import type { WorkoutPlan } from "../types/WorkoutPlan";
import type { WorkoutPlanExercise } from "../types/WorkoutPlanExercise";
import { exerciseImages } from "../data/exerciseImages";
import "../styles/Navigation.css";

type RequestState<T> =
  | { status: "idle" }
  | { status: "loading" }
  | { status: "success"; data: T }
  | { status: "error"; error: Error };

function PlanDetails() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [state, setState] = useState<RequestState<WorkoutPlan>>({
    status: "loading",
  });

  const [exerciseState, setExerciseState] = useState<
    RequestState<WorkoutPlanExercise[]>
  >({
    status: "loading",
  });

  // Load the selected workout plan
  useEffect(() => {
    if (!id) {
      return;
    }

    api
      .getWorkoutPlan(Number(id))
      .then((workoutPlan) => {
        setState({
          status: "success",
          data: workoutPlan,
        });
      })
      .catch((error: Error) => {
        setState({
          status: "error",
          error,
        });
      });
  }, [id]);

  // Load exercises belonging to the selected workout plan
  useEffect(() => {
    if (!id) {
      return;
    }

    api
      .getWorkoutPlanExercises(Number(id))
      .then((workoutPlanExercises) => {
        setExerciseState({
          status: "success",
          data: workoutPlanExercises,
        });
      })
      .catch((error: Error) => {
        console.error(error);

        setExerciseState({
          status: "error",
          error,
        });
      });
  }, [id]);

  // Render the exercise section
  // TODO: Mobile only - show the first 4 exercises, then allow Show more / Show less.
  function renderWorkoutPlanExercises() {
    if (exerciseState.status === "loading") {
      return <Text c="dimmed">Loading exercises...</Text>;
    }

    if (exerciseState.status === "error") {
      return <Text c="red">Unable to load exercises. Please try again.</Text>;
    }

    if (exerciseState.status === "success") {
      if (exerciseState.data.length === 0) {
        return <Text c="dimmed">No exercises in this plan yet.</Text>;
      }

      return (
        <Stack gap="sm">
          
          {exerciseState.data.map((workoutPlanExercise) => (
            <Paper
              key={workoutPlanExercise.id}
              withBorder
              p="md"
              radius="md"
              onClick={() =>
                navigate(
                  `/workout-plans/${id}/exercises/${workoutPlanExercise.id}`,
                )
              }
              style={{ cursor: "pointer" }}
            >
              <Group gap="md" wrap="nowrap">
                {exerciseImages[workoutPlanExercise.exerciseName] && (
                  <Image
                    src={exerciseImages[workoutPlanExercise.exerciseName]}
                    alt={workoutPlanExercise.exerciseName}
                    w={100}
                    h={80}
                    radius="md"
                    fit="cover"
                    style={{ flexShrink: 0 }}
                  />
                )}

                <Stack gap={2}>
                  <Text fw={600}>{workoutPlanExercise.exerciseName}</Text>

                  <Text size="sm" c="dimmed">
                    {workoutPlanExercise.muscleGroup}
                  </Text>

                  <Text size="sm">
                    {workoutPlanExercise.targetSets} sets ×{" "}
                    {workoutPlanExercise.targetReps} reps
                  </Text>
                </Stack>
              </Group>
            </Paper>
          ))}
        </Stack>
      );
    }

    return null;
  }

  if (state.status === "loading") {
    return (
      <Container size="lg" py="xl">
        <Text c="dimmed">Loading workout plan...</Text>
      </Container>
    );
  }

  if (state.status === "error") {
    return (
      <Container size="lg" py="xl">
        <Text c="red">Unable to load workout plan. Please try again.</Text>
      </Container>
    );
  }

  if (state.status === "success") {
    return (
      <Container size="lg" py="xl">
        <Stack gap="lg">
          <Anchor
            component={Link}
            to={`/`}
            className="backLink"
            underline="never"
          >
            <IconArrowLeft size={18} />
            Back to workout plans
          </Anchor>
          <Title order={2}>{state.data.name}</Title>

          {state.data.description && (
            <Text c="dimmed">{state.data.description}</Text> //TODO: Mobile only - add Show more / Show less for long plan descriptions.
          )}
          <Title order={3}>Exercises</Title>

          {renderWorkoutPlanExercises()}
        </Stack>
      </Container>
    );
  }

  return null;
}

export default PlanDetails;
