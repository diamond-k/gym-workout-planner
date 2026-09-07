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
import { api } from "../services/api";
import { exerciseImages } from "../data/exerciseImages";
import type { WorkoutPlanExercise } from "../types/WorkoutPlanExercise";
import type { RequestState } from '../types/RequestState';
import "../styles/ExerciseDetails.css";
import "../styles/Navigation.css";


function ExerciseDetails() {
  const { id, workoutPlanExerciseId } = useParams();

  const [state, setState] = useState<RequestState<WorkoutPlanExercise>>({
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
          throw new Error("Exercise not found");
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
    return (
      <Container size="lg" py="xl">
        <Text c="red">
          Unable to load exercise. Please try again.
        </Text>
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
            Back to workout plan
          </Anchor>

          {imageSrc && (
            <Image
              src={imageSrc}
              alt={state.data.exerciseName}
              radius="md"
              className={`exerciseDetailImage ${
                state.data.exerciseName === "Tricep Pushdown"
                  ? "tricepPushdownImage"
                  : ""
              }`}
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