import { Button, Container, Stack, Textarea, TextInput, Title } from '@mantine/core';
import { useState } from 'react';

function CreateEditWorkoutPlan() {
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');

  return (
    <Container size="lg" py="xl">
      <Stack gap="lg">
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
        <Button color="pink">
          Save Plan
        </Button>
      </Stack>
    </Container>
  );
}

export default CreateEditWorkoutPlan;