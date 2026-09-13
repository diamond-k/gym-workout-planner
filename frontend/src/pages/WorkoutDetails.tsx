import { useEffect, useState } from "react";
import { useNavigate, useParams, Link } from "react-router";
import {
  Container,
  Paper,
  Button,
  Stack,
  Text,
  Title,
  Group,
  Anchor,
  Modal,
  ActionIcon,
} from "@mantine/core";
import { IconArrowLeft, IconPencil, IconTrash } from "@tabler/icons-react";
import { api, ApiError } from "../services/api";
import type { WorkoutPlanResponse } from "../types/WorkoutPlanResponse";
import type { WorkoutPlanExerciseResponse } from "../types/WorkoutPlanExerciseResponse";
import type { RequestState } from "../types/RequestState";
import ExerciseImage from "../components/ExerciseImage";
import "../styles/WorkoutDetails.css";

function WorkoutDetails() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [state, setState] = useState<RequestState<WorkoutPlanResponse>>({
    status: "loading",
  });

  const [exerciseState, setExerciseState] = useState<
    RequestState<WorkoutPlanExerciseResponse[]>
  >({
    status: "loading",
  });

  const [deleteModalOpen, setDeleteModalOpen] = useState(false);
  const [deleteError, setDeleteError] = useState("");

  // Load the selected workout
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

  // Load exercises belonging to the selected workout
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

  function handleEditWorkout() {
    navigate(`/workout-plans/${id}/edit`);
  }

  function openDeleteModal() {
    setDeleteError("");
    setDeleteModalOpen(true);
  }

  function closeDeleteModal() {
    setDeleteError("");
    setDeleteModalOpen(false);
  }

  async function handleDeleteWorkout() {
    if (!id) {
      return;
    }

    setDeleteError("");

    try {
      await api.deleteWorkoutPlan(Number(id));
      navigate("/");
    } catch (error) {
      console.error(error);
      setDeleteError("Unable to delete workout. Please try again.");
    }
  }

  function renderWorkoutExercises() {
    if (exerciseState.status === "loading") {
      return <Text c="dimmed">Loading exercises...</Text>;
    }

    if (exerciseState.status === "error") {
      return (
        <Text c="red">
          Unable to load exercises. Please try again.
        </Text>
      );
    }

    if (exerciseState.status === "success") {
      if (exerciseState.data.length === 0) {
        return (
          <Text c="dimmed">
            No exercises in this workout yet.
          </Text>
        );
      }

      return (
        <Stack gap="sm">
          {exerciseState.data.map((workoutPlanExercise) => (
            <Paper
              key={workoutPlanExercise.id}
              withBorder
              shadow="sm"
              p="md"
              radius="md"
              className="workoutExerciseCard"
              onClick={() =>
                navigate(
                  `/workout-plans/${id}/exercises/${workoutPlanExercise.id}`,
                )
              }
            >
              <Group gap="md" wrap="nowrap">
                <ExerciseImage
                  exerciseName={workoutPlanExercise.exerciseName}
                />

                <Stack gap={2}>
                  <Text fw={600}>
                    {workoutPlanExercise.exerciseName}
                  </Text>

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
        <Text c="dimmed">
          Loading workout...
        </Text>
      </Container>
    );
  }

  if (state.status === "error") {
    const workoutNotFound =
      state.error instanceof ApiError &&
      state.error.status === 404;

    return (
      <Container size="lg" py="xl">
        <Stack gap="md">
          {workoutNotFound ? (
            <>
              <Title order={2}>
                Workout not found
              </Title>

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
              Unable to load workout. Please try again.
            </Text>
          )}
        </Stack>
      </Container>
    );
  }

  if (state.status === "success") {
    return (
      <>
        <Modal
          opened={deleteModalOpen}
          onClose={closeDeleteModal}
          title={<Text fw={700}>Delete workout?</Text>}
          centered
        >
          <Stack>
            <Text>
              Are you sure you want to delete "{state.data.name}"?
              This action cannot be undone.
            </Text>

            {deleteError && (
              <Text c="red">
                {deleteError}
              </Text>
            )}

            <Group justify="flex-end">
              <Button
                variant="default"
                onClick={closeDeleteModal}
              >
                Cancel
              </Button>

              <Button
                color="pink"
                onClick={handleDeleteWorkout}
              >
                Delete
              </Button>
            </Group>
          </Stack>
        </Modal>

        <Container size="lg" py="xl">
          <Stack gap="lg">
            <Anchor
              component={Link}
              to="/"
              className="backLink"
              underline="never"
            >
              <IconArrowLeft size={18} />
              Back to workouts
            </Anchor>

            <Group
              justify="space-between"
              align="flex-start"
              wrap="nowrap"
              className="planHeader"
            >
              <Title order={2} className="planTitle">
                {state.data.name}
              </Title>

              <Group
                gap="sm"
                wrap="nowrap"
                className="planActions"
              >
                {/* Desktop */}
                <Button
                  variant="outline"
                  color="pink"
                  className="planActionDesktop"
                  onClick={handleEditWorkout}
                >
                  Edit
                </Button>

                <Button
                  color="pink"
                  className="planActionDesktop"
                  onClick={openDeleteModal}
                >
                  Delete
                </Button>

                {/* Mobile */}
                <ActionIcon
                  variant="outline"
                  color="pink"
                  size="lg"
                  className="planActionMobile"
                  aria-label="Edit workout"
                  onClick={handleEditWorkout}
                >
                  <IconPencil size={18} />
                </ActionIcon>

                <ActionIcon
                  color="pink"
                  size="lg"
                  className="planActionMobile"
                  aria-label="Delete workout"
                  onClick={openDeleteModal}
                >
                  <IconTrash size={18} />
                </ActionIcon>
              </Group>
            </Group>

            {state.data.description && (
              <Text c="dimmed">
                {state.data.description}
              </Text>
            )}

            <Title order={3}>
              Exercises
            </Title>

            {renderWorkoutExercises()}
          </Stack>
        </Container>
      </>
    );
  }

  return null;
}

export default WorkoutDetails;