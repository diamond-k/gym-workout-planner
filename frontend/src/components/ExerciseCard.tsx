import {
  Badge,
  Button,
  Divider,
  Group,
  NumberInput,
  Paper,
  Stack,
  Text,
} from "@mantine/core";
import { IconPlus, IconTrash } from "@tabler/icons-react";
import type { Exercise } from "../types/Exercise";
import ExerciseImage from "../components/ExerciseImage";

// discriminated union: variant "available" requires onAdd
// variant "selected" requires onRemove/onUpdateTargets/targetSets/targetReps
type ExerciseCardProps =
  | {
      variant: "available";
      exercise: Exercise;
      onAdd: (exercise: Exercise) => void;
    }
  | {
      variant: "selected";
      exercise: Exercise;
      targetSets: number | string;
      targetReps: number | string;
      onRemove: (exerciseId: number) => void;
      onUpdateTargets: (
        exerciseId: number,
        field: "targetSets" | "targetReps",
        value: number | string,
      ) => void;
    };

function ExerciseCard(props: ExerciseCardProps) {
  const { exercise } = props;

  return (
    <Paper withBorder p="md" radius="md">
      <div
        className={
          props.variant === "available"
            ? "availableExerciseCard"
            : "selectedExerciseCard"
        }
      >
        <Group
          gap="md"
          wrap="nowrap"
          className="exerciseIdentity"
          style={{ minWidth: 0 }}
        >
  
          <ExerciseImage exerciseName={exercise.name} />


          <Stack gap={4} style={{ minWidth: 0 }}>
            <Text fw={600}>{exercise.name}</Text>
            <Badge color="var(--mantine-color-pink-6)" variant="light">
              {exercise.muscleGroup}
            </Badge>
          </Stack>
        </Group>

        {props.variant === "available" ? (
          <Button
            type="button"
            className="exerciseActionButton availableExerciseAction"
            color="var(--mantine-color-pink-6)"
            variant="light"
            aria-label={`Add ${exercise.name}`}
            onClick={() => props.onAdd(exercise)}
          >
            <span className="actionText">Add</span>
            <IconPlus className="actionIcon" size={18} />
          </Button>
        ) : (
          <>
            <Button
              type="button"
              className="exerciseActionButton selectedExerciseAction"
              color="var(--mantine-color-pink-6)"
              variant="outline"
              aria-label={`Remove ${exercise.name}`}
              onClick={() => props.onRemove(exercise.id)}
            >
              <span className="actionText">Remove</span>
              <IconTrash className="actionIcon" size={18} />
            </Button>

            <Divider className="selectedExerciseDivider" />

            <Group gap="sm" wrap="wrap" className="exerciseTargets">
              <Group gap={6} wrap="nowrap" className="targetRow">
                <Text size="sm" fw={500}>
                  Sets
                </Text>
                <NumberInput
                  min={1}
                  className="targetInput"
                  value={props.targetSets}
                  onChange={(value) =>
                    props.onUpdateTargets(exercise.id, "targetSets", value)
                  }
                  onBlur={(event) => {
                    const raw = event.currentTarget.value;
                    const parsed = raw === "" ? 1 : Math.max(1, Number(raw));
                    props.onUpdateTargets(exercise.id, "targetSets", parsed);
                  }}
                />
              </Group>

              <Group gap={6} wrap="nowrap" className="targetRow">
                <Text size="sm" fw={500}>
                  Reps
                </Text>
                <NumberInput
                  min={1}
                  className="targetInput"
                  value={props.targetReps}
                  onChange={(value) =>
                    props.onUpdateTargets(exercise.id, "targetReps", value)
                  }
                  onBlur={(event) => {
                    const raw = event.currentTarget.value;
                    const parsed = raw === "" ? 1 : Math.max(1, Number(raw));
                    props.onUpdateTargets(exercise.id, "targetReps", parsed);
                  }}
                />
              </Group>
            </Group>
          </>
        )}
      </div>
    </Paper>
  );
}

export default ExerciseCard;
