import {
  Badge,
  Button,
  Container,
  Group,
  Image,
  NumberInput,
  Paper,
  Select,
  SimpleGrid,
  Stack,
  Text,
  Textarea,
  TextInput,
  Title,
  Divider
} from '@mantine/core';
import { useEffect, useState } from 'react';
import { api } from '../services/api';
import { exerciseImages } from '../data/exerciseImages';
import type { Exercise } from '../types/Exercise';
import type { MuscleGroup } from '../types/MuscleGroup';
import type { RequestState } from '../types/RequestState';
import '../styles/CreateEditWorkoutPlan.css';

type SelectedExercise = {
  exercise: Exercise;
  targetSets: number | string;
  targetReps: number | string;
};

const muscleGroups: MuscleGroup[] = [
  'CHEST',
  'BACK',
  'SHOULDERS',
  'BICEPS',
  'TRICEPS',
  'LEGS',
  'GLUTES',
  'CORE',
];

function CreateEditWorkoutPlan() {
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [selectedMuscleGroup, setSelectedMuscleGroup] = useState<MuscleGroup | null>(null);
  const [selectedExercises, setSelectedExercises] = useState<SelectedExercise[]>([]);
  const [exerciseState, setExerciseState] =
    useState<RequestState<Exercise[]>>({
      status: 'loading',
    });

  useEffect(() => {
    api
      .getExercises()
      .then((exercises) => {
        setExerciseState({
          status: 'success',
          data: exercises,
        });
      })
      .catch((error: Error) => {
        console.error(error);

        setExerciseState({
          status: 'error',
          error,
        });
      });
  }, []);

  const availableExercises =
    exerciseState.status === 'success'
      ? exerciseState.data.filter((exercise) => {
          const isAlreadySelected = selectedExercises.some(
            (selected) => selected.exercise.id === exercise.id,
          );

          const matchesMuscleGroup =
            selectedMuscleGroup === null ||
            exercise.muscleGroup === selectedMuscleGroup;

          return !isAlreadySelected && matchesMuscleGroup;
        })
      : [];

  function handleAddExercise(exercise: Exercise) {
    setSelectedExercises((current) => [
      ...current,
      {
        exercise,
        targetSets: 3,
        targetReps: 10,
      },
    ]);
  }

  function handleRemoveExercise(exerciseId: number) {
    setSelectedExercises((current) =>
      current.filter((item) => item.exercise.id !== exerciseId),
    );
  }

  function handleUpdateTargets(
    exerciseId: number,
    field: 'targetSets' | 'targetReps', 
    value: number | string) {
    setSelectedExercises((current) =>
      current.map((item) =>
        item.exercise.id === exerciseId ? { ...item, [field]: value } : item
      )
    );
  }

  return (
    <Container size="lg" py="xl">
      <Stack gap="xl">
        <Title order={2}>Create Workout Plan</Title>

        <TextInput
          label="Name"
          placeholder="e.g. Upper Body Day"
          value={name}
          onChange={(event) => setName(event.currentTarget.value)}
          required
        />

        <Textarea
          label="Description"
          placeholder="Add an optional description"
          value={description}
          onChange={(event) => setDescription(event.currentTarget.value)}
          minRows={3}
        />
        <Select
          label="Filter by muscle group"
          placeholder="All"
          data={muscleGroups}
          value={selectedMuscleGroup}
          onChange={(value) =>
            setSelectedMuscleGroup(value as MuscleGroup | null)
          }
          searchable
          clearable/>

        <SimpleGrid cols={{ base: 1, md: 2 }} spacing="xl">        
          {/*Available exercises*/}
          <Stack gap="md">

            <Title order={3}>Choose Exercises</Title>
              <Paper withBorder p="md" radius="md" className="exerciseList">
                <Stack gap="sm">
                  {exerciseState.status === 'loading' && (
                    <Text c="dimmed">Loading exercises...</Text>
                  )}

                  {exerciseState.status === 'error' && (
                    <Text c="red">Unable to load exercises. Please try again.</Text>
                  )}

                  {exerciseState.status === 'success' &&
                    availableExercises.length === 0 && (
                      <Text c="dimmed">
                        {selectedMuscleGroup
                          ? 'No available exercises for this muscle group.'
                          : 'All available exercises have been added to the plan.'}
                      </Text>
                  )}

                  {availableExercises.map((exercise) => (
                    <Paper key={exercise.id} withBorder p="md" radius="md">
                      <Group justify="space-between">
                        <Group gap="md" wrap="nowrap">
                          {exerciseImages[exercise.name] && (
                            <Image
                              src={exerciseImages[exercise.name]}
                              alt={exercise.name}
                              w={80}
                              h={70}
                              radius="md"
                              fit="cover"
                              style={{ flexShrink: 0 }}
                            />
                          )}

                          <Stack gap={4}>
                            <Text fw={600}>{exercise.name}</Text>
                            <Badge color="var(--mantine-color-pink-6)" variant="light">
                              {exercise.muscleGroup}
                            </Badge>
                          </Stack>
                        </Group>

                        <Button
                          color="var(--mantine-color-pink-6)"
                          variant="light"
                          onClick={() => handleAddExercise(exercise)}>
                          Add
                        </Button>
                    </Group>
                  </Paper>
                ))}
                </Stack>
              </Paper>
          </Stack>
         
          {/*Added exercises*/}
          <Stack gap="md">
            <Title order={3}>In This Plan</Title>
            <Paper withBorder p="md" radius="md" className="exerciseList">
              <Stack gap="sm">
                {selectedExercises.length === 0 && (
                  <Text c="dimmed">
                    No exercises added to this workout plan yet.
                  </Text>
                )}

                {selectedExercises.map((selected) => (
                  <Paper key={selected.exercise.id} withBorder p="md" radius="md">
                    <Stack gap="sm">
                      <Group justify="space-between">
                        <Group gap="md" wrap="nowrap">
                          {exerciseImages[selected.exercise.name] && (
                            <Image
                              src={exerciseImages[selected.exercise.name]}
                              alt={selected.exercise.name}
                              w={80}
                              h={70}
                              radius="md"
                              fit="cover"
                              style={{ flexShrink: 0 }}
                            />
                          )}

                          <Stack gap={4}>
                            <Text fw={600} lineClamp={1}>{selected.exercise.name}</Text>
                            <Badge color="var(--mantine-color-pink-6)" variant="light">
                              {selected.exercise.muscleGroup}
                            </Badge>
                          </Stack>
                        </Group>

                        <Button
                          color="var(--mantine-color-pink-6)"
                          variant="outline"
                          onClick={() => handleRemoveExercise(selected.exercise.id)}
                        >
                          Remove
                        </Button>
                      </Group>

                      <Divider />

                      <Group gap="sm" wrap="nowrap">
                        <Group gap={6} wrap="nowrap">
                          <Text size="sm" fw={500}>Sets</Text>
                          <NumberInput
                            min={1}
                            w={70}
                            value={selected.targetSets}
                            onChange={(value) =>
                              handleUpdateTargets(selected.exercise.id, 'targetSets', value)
                            }
                            onBlur={(event) => {
                              const raw = event.currentTarget.value;
                              const parsed = raw === '' ? 1 : Math.max(1, Number(raw));
                              handleUpdateTargets(selected.exercise.id, 'targetSets', parsed);
                            }}
                          />
                        </Group>

                        <Group gap={6} wrap="nowrap">
                          <Text size="sm" fw={500}>Reps</Text>
                          <NumberInput
                            min={1}
                            w={70}
                            value={selected.targetReps}
                            onChange={(value) =>
                              handleUpdateTargets(selected.exercise.id, 'targetReps', value)
                            }
                            onBlur={(event) => {
                              const raw = event.currentTarget.value;
                              const parsed = raw === '' ? 1 : Math.max(1, Number(raw));
                              handleUpdateTargets(selected.exercise.id, 'targetReps', parsed);
                            }}
                          />
                        </Group>
                      </Group>
                    </Stack>
                  </Paper>
                ))}
              </Stack>
            </Paper>
          </Stack>
        </SimpleGrid>
        <Button color="pink">Save Plan</Button>
      </Stack>
    </Container>
  );
}

export default CreateEditWorkoutPlan;