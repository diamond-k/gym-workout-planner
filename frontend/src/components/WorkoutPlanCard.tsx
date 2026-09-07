import { useNavigate } from "react-router";
import { Paper, Stack, Text, ThemeIcon, Title } from "@mantine/core";
import { IconBarbell } from "@tabler/icons-react";
import type { WorkoutPlan } from "../types/WorkoutPlan";
import "../styles/WorkoutPlanCard.css";

interface WorkoutPlanCardProps {
  workoutPlan: WorkoutPlan;
  exerciseCount: number;
}

function WorkoutPlanCard({workoutPlan, exerciseCount}: WorkoutPlanCardProps) {
  const navigate = useNavigate();

  return (
    <Paper
      withBorder
      shadow="sm"
      p="lg"
      radius="md"
      className="workoutPlanCard"
      onClick={()=> navigate(`/workout-plans/${workoutPlan.id}`)}
    >
      <Stack gap="xs" align="center">
        <ThemeIcon
          size={64}
          radius="xl"
          variant="light"
          className="workoutPlanCardIcon">
          <IconBarbell size={30} />
        </ThemeIcon>

        <Title order={4} ta="center">
          {workoutPlan.name}
        </Title>

        {workoutPlan.description && (
          <Text c="dimmed" lineClamp={2} ta="center" w="100%">
            {workoutPlan.description}
          </Text>
        )}
        
        <Text size="sm" c="dimmed">
          {exerciseCount === 0
            ? "No exercises yet"
            : exerciseCount === 1
              ? "1 exercise"
              : `${exerciseCount} exercises`}
        </Text>
      </Stack>
    </Paper>
  );
}

export default WorkoutPlanCard;
