import { useEffect, useState } from "react";
import { Link, useParams } from "react-router";
import {
  Anchor,
  Container,
  Image,
  Stack,
  Text,
  Title,
  Badge
} from "@mantine/core";
import { IconArrowLeft } from "@tabler/icons-react";
import { api, ApiError } from "../services/api";
import { exerciseImages } from "../data/exerciseImages";
import type { WorkoutPlanExerciseResponse } from "../types/WorkoutPlanExerciseResponse";
import type { RequestState } from '../types/RequestState';
import "../styles/ExerciseDetails.css";

class ExerciseNotFoundError extends Error {
  constructor() {
    super("Exercise not found");
    this.name = "ExerciseNotFoundError";
  }
}

function ExerciseDetails() {
  const { id, workoutPlanExerciseId } = useParams();

  const [state, setState] = useState<RequestState<WorkoutPlanExerciseResponse>>({
    status: "loading",
  });

  // Load the selected exercise from the workout plan
  useEffect(() => {
    if (!id || !workoutPlanExerciseId) {
      return;
    }

    api
      .getWorkoutPlanExercises(Number(id))
      .then((workoutPlanExercises) => {
        const selectedExercise = workoutPlanExercises.find(
          (exercise) => exercise.id === Number(workoutPlanExerciseId),
        );

        if (!selectedExercise) {
          throw new ExerciseNotFoundError();
        }

        setState({
          status: "success",
          data: selectedExercise,
        });
      })
      .catch((error: Error) => {
        console.error(error);

        setState({
          status: "error",
          error,
        });
      });
  }, [id, workoutPlanExerciseId]);

  if (state.status === "loading") {
    return (
      <Container size="lg" py="xl">
        <Text c="dimmed">Loading exercise...</Text>
      </Container>
    );
  }

  if (state.status === "error") {
    const exerciseNotFound =
      state.error instanceof ExerciseNotFoundError;

    const workoutNotFound =
      state.error instanceof ApiError &&
      state.error.status === 404;

    return (
      <Container size="lg" py="xl">
        <Stack gap="md">
          {exerciseNotFound ? (
            <>
              <Title order={2}>Exercise not found</Title>

              <Text c="dimmed">
                This exercise may have been removed from the workout or the link may be incorrect.
              </Text>

              <Anchor
                component={Link}
                to={`/workout-plans/${id}`}
                className="backLink"
                underline="never"
              >
                <IconArrowLeft size={18} />
                Back to workout
              </Anchor>
            </>
          ) : workoutNotFound ? (
            <>
              <Title order={2}>Workout not found</Title>

              <Text c="dimmed">
                This workout may have been deleted or the link may be incorrect.
              </Text>

              <Anchor
                component={Link}
                to="/"
                className="backLink"
                underline="never"
              >
                <IconArrowLeft size={18} />
                Back to workouts
              </Anchor>
            </>
          ) : (
            <Text c="red">
              Unable to load exercise. Please try again.
            </Text>
          )}
        </Stack>
      </Container>
    );
  }

  if (state.status === "success") {
    const imageSrc = exerciseImages[state.data.exerciseName];

    return (
      <Container size="lg" py="xl">
        <Stack gap="lg">
          <Anchor
            component={Link}
            to={`/workout-plans/${id}`}
            className="backLink"
            underline="never"
          >
            <IconArrowLeft size={18} />
            Back to workout
          </Anchor>

          {imageSrc && (
            <Image
              src={imageSrc}
              alt={state.data.exerciseName}
              radius="md"
              className="exerciseDetailImage"
            />
          )}
          <Stack gap="xs">
            <Title order={2}>
              {state.data.exerciseName}
            </Title>
            <Badge color="var(--mantine-color-pink-6)" variant="light">
              {state.data.muscleGroup}
            </Badge>
            <Text>
              {state.data.targetSets} sets × {state.data.targetReps} reps
            </Text>
          </Stack>
          <Stack gap="xs">
            <Title order={3}>Instructions</Title>
            <Text>
              {state.data.instructions}
            </Text>
          </Stack>
        </Stack>
      </Container>
    );
  }

  return null;
}

export default ExerciseDetails;