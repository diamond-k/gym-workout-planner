import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router';
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
import { IconPlus, IconTrash } from '@tabler/icons-react';
import { api } from '../services/api';
import { exerciseImages } from '../data/exerciseImages';
import type { Exercise } from '../types/Exercise';
import type { MuscleGroup } from '../types/MuscleGroup';
import type { RequestState } from '../types/RequestState';
import type { WorkoutPlan } from '../types/WorkoutPlan';
import type { WorkoutPlanExercise } from '../types/WorkoutPlanExercise';
import '../styles/CreateEditWorkoutPlan.css';

type SelectedExercise = {
  exercise: Exercise;
  targetSets: number | string;
  targetReps: number | string;
};

type EditLoadData = {
  workoutPlan: WorkoutPlan;
  workoutPlanExercises: WorkoutPlanExercise[];
};

interface FormErrors {
  name?: string;
  exercises?: string;
}

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
  
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');

  const { id } = useParams();
  const navigate = useNavigate();
  
  const isEditMode = id !== undefined;

  const [editState, setEditState] =
  useState<RequestState<EditLoadData>>(
    isEditMode
      ? { status: 'loading' }
      : { status: 'idle' }
  );

  const [formErrors, setFormErrors] = useState<FormErrors>({});

  // load the exercise catalogue for both create and edit
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

  // when editing, load the existing workout plan and its saved exercises
  useEffect(() => {

    if(!isEditMode || id === undefined){
      return;
    }

    const planId = Number(id);

    Promise.all([
      api.getWorkoutPlan(planId),
      api.getWorkoutPlanExercises(planId),
    ])
    .then(([workoutPlan, workoutPlanExercises]) => {
      setName(workoutPlan.name);
      setDescription(workoutPlan.description ?? '');

      const selected = workoutPlanExercises.map((planExercise) => ({
        exercise: {
          id: planExercise.exerciseId,
          name: planExercise.exerciseName,
          muscleGroup: planExercise.muscleGroup,
          instructions: planExercise.instructions
        },
        targetSets: planExercise.targetSets,
        targetReps: planExercise.targetReps
      }))

      setSelectedExercises(selected);

      setEditState({
        status: 'success',
        data: {
          workoutPlan,
          workoutPlanExercises,
        },
      });

    }).catch((error: Error) => {
      console.error(error);
      setEditState({
        status: 'error',
        error,
      });
    });   
  }, [id, isEditMode]);

  // derived array - recalculated whenever the component re-renders
  const availableExercises =
    exerciseState.status === 'success'
      ? exerciseState.data.filter((exercise) => {
        
        // check whether this exercise is already in the selected list
        const isAlreadySelected = 
          selectedExercises.some((selected) => selected.exercise.id === exercise.id);

        // check the muscle group filter
        // if no muscle group is selected (null), show all exercises
        // or, only show exercises matching the selected muscle group
        const matchesMuscleGroup =
          selectedMuscleGroup === null ||
          exercise.muscleGroup === selectedMuscleGroup;

          // only include the exercise if:
          // it is NOT already in the plan, AND
          // it matches the current muscle group filter
          return !isAlreadySelected && matchesMuscleGroup;
        })
        .sort((a,b) => a.name.localeCompare(b.name))
      : [];

  // add the clicked exercise to selectedExercises
  function handleAddExercise(exercise: Exercise) {
    // React calls the function setSelectedExercises
    // the paramter exercises is the current value of selectedExercises 
    // at the moment React performs this update
    setSelectedExercises((exercises) => [
      // keep all exercises already in the plan...
      ...exercises,
      // then add this new exercise, with starting sets/reps values
      {
        exercise,
        targetSets: 3,
        targetReps: 10,
      },
    ]);

    // clear the exercise validation error once an exercise is added
    setFormErrors((errors) => ({
      ...errors,
      exercises: undefined,
    }));
  }

  // remove the clicked exercise from selectedExercises
  function handleRemoveExercise(exerciseId: number) {
    setSelectedExercises((exercises) =>
      // keep every selected exercise EXCEPT the one whose id matches exerciseId.
      exercises.filter((item) => item.exercise.id !== exerciseId),
    );
  }

  // update either the sets or reps for one exercise already in the plan
  function handleUpdateTargets(exerciseId: number, field: 'targetSets' | 'targetReps', value: number | string) {
    // React provides the current selectedExercises array
    setSelectedExercises((exercises) =>
      // map() goes through every selected exercise and creates a new array
      exercises.map((item) => 
        // if this is the exercise being edited...
        item.exercise.id === exerciseId 
        // ...create a new copy of that exercise item
        // ...item - keep all of the item's existing properties
        // [field]: value - change only the field passed in:
        // either targetSets or targetReps
        ? { ...item, [field]: value } 
        : item
      )
    );
  }

  function validate(planName: string, exercises: SelectedExercise[]): FormErrors {
    // create an empty object to store any validation errors
    const errors: FormErrors = {};

    // if name is empty after removing spaces, add a name error
    if (planName.trim() === '') {
      errors.name = 'Name is required.';
    }

    // if selected exercises list is empty, add a exercise error
    if (exercises.length === 0) {
      errors.exercises = 'At least one exercise is required.';
    }

    // return the errors object back to calling function
    return errors;
  }

  async function handleSubmit(event: React.SubmitEvent<HTMLFormElement>) {
    // stop browser doing its default form submission/reload
    event.preventDefault();

    // pass the current name state into validate()
    // validate() returns either {} or an object containing validation errors
    const nextErrors = validate(name, selectedExercises);

    // store returned validation errors in React state
    // so they can be displayed in the form
    setFormErrors(nextErrors);

    // Object.keys() gets the property names from nextErrors.
    // if there is at least one property, there is at least one error
    if (Object.keys(nextErrors).length > 0) {
      return;
    }

    // clear any error from a previous save attempt
    setError('');

    // tell React the form is currently being saved
    setSaving(true);

    const planInput = {
        name: name.trim(),
        description: description.trim() || null,
        exercises: selectedExercises.map((selected) => ({
        exerciseId: selected.exercise.id,
        targetSets: Number(selected.targetSets),
        targetReps: Number(selected.targetReps)
      })),
    };

    try {
      if(isEditMode && id !== undefined){
        const updatedPlan = await api.updateWorkoutPlan(Number(id), planInput);
        navigate(`/workout-plans/${updatedPlan.id}`);

      }else{
        const createdPlan = await api.createWorkoutPlan(planInput);
        navigate(`/workout-plans/${createdPlan.id}`);
      }
    }
    catch(error){
      // if the POST fails, show user a readable error message.
      setError(`Unable to save workout plan: ${(error as Error).message}`);
    }
    finally{
      setSaving(false);
    }
  }
  return (
    <Container size="lg" py="xl">
      <form onSubmit={handleSubmit}>
        <Stack gap="xl">
          <Title order={2}> 
            {isEditMode ? 'Edit Workout Plan' : 'Create Workout Plan'}
          </Title>

          {isEditMode && editState.status === 'loading' && (
            <Text c="dimmed">Loading workout plan...</Text>
          )}

          {isEditMode && editState.status === 'error' && (
            <Text c="red">Unable to load workout plan. Please try again.</Text>
          )}

         <TextInput
            label="Name"
            placeholder="e.g. Upper Body Day"
            value={name}
            styles={{
              error: {
                fontSize: '15px',
              },
            }}
            onChange={(event) => {
              setName(event.currentTarget.value);
              // clear the name validation error as soon as the user starts fixing it
              setFormErrors((errors) => ({
                ...errors,
                name: undefined,
              }));
            }}
            // tells Mantine to show the validation message under the input
            error={formErrors.name}
            withAsterisk/>

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
                        <Group justify="space-between" wrap="nowrap" w="100%">
                          <Group gap="md" wrap="nowrap" style={{ flex: 1, minWidth: 0 }}>
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

                            <Stack gap={4} style={{ minWidth: 0 }}>
                              <Text fw={600}>{exercise.name}</Text>
                              <Badge color="var(--mantine-color-pink-6)" variant="light">
                                {exercise.muscleGroup}
                              </Badge>
                            </Stack>
                          </Group>

                          <Button
                            type="button"
                            style={{ flexShrink: 0 }}
                            className="exerciseActionButton"
                            color="var(--mantine-color-pink-6)"
                            variant="light"
                            aria-label={`Add ${exercise.name}`}
                            onClick={() => handleAddExercise(exercise)}>
                            <span className="actionText">Add</span>
                            <IconPlus className="actionIcon" size={18} />
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
                        No exercises have been added yet.
                      </Text>
                    )}

                    {selectedExercises.map((selected) => (
                      <Paper key={selected.exercise.id} withBorder p="md" radius="md">
                        <Stack gap="sm">
                          <Group justify="space-between" wrap="nowrap" w="100%">
                            <Group gap="md" wrap="nowrap" style={{ flex: 1, minWidth: 0 }}>
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

                              <Stack gap={4} style={{ minWidth: 0 }}>
                                <Text fw={600}>{selected.exercise.name}</Text>
                                <Badge color="var(--mantine-color-pink-6)" variant="light">
                                  {selected.exercise.muscleGroup}
                                </Badge>
                              </Stack>
                            </Group>

                            <Button
                              type="button"
                              style={{ flexShrink: 0 }}
                              className="exerciseActionButton"
                              color="var(--mantine-color-pink-6)"
                              variant="outline"
                              aria-label={`Remove ${selected.exercise.name}`}
                              onClick={() => handleRemoveExercise(selected.exercise.id)}>
                              <span className="actionText">Remove</span>
                              <IconTrash className="actionIcon" size={18} />
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
                {formErrors.exercises && (<Text c="red">{formErrors.exercises}</Text>)}
              </Stack>
            </SimpleGrid>
            {error && <Text c="red">{error}</Text>}
          <Button type="submit" className='savePlanButton' loading={saving} color="pink">Save Plan</Button>
        </Stack>    
      </form>
    </Container>
  );
}

export default CreateEditWorkoutPlan;