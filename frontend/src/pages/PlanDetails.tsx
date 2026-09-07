import { useEffect, useState } from "react";
import { useParams } from "react-router";
import { Container, Text, Title, Stack } from "@mantine/core";
import { api } from "../services/api";
import type { WorkoutPlan } from "../types/WorkoutPlan";

type RequestState<T> =
  | { status: "idle" }
  | { status: "loading" }
  | { status: "success"; data: T }
  | { status: "error"; error: Error };

function PlanDetails() {
  const { id } = useParams();

  const [state, setState] = useState<RequestState<WorkoutPlan>>({
    status: "loading",
  });

  // Load the selected workout plan when the page appears
  useEffect(() => {
    if (!id) {
      return;
    }

    api.getWorkoutPlan(Number(id))
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

  if (state.status === "loading") {
    return <Text c="dimmed">Loading workout plan...</Text>;
  }

  if (state.status === "error") {
    return <Text c="red">{state.error.message}</Text>;
  }

  if (state.status === "success") {
    return (
      <Container size="lg" py="xl">
       <Stack gap="sm">
          <Title order={2}>
            {state.data.name}
          </Title>
          <Text c="dimmed">
            {state.data.description}
          </Text>
        </Stack>
      </Container>
    );
  }

  return null;
}

export default PlanDetails;