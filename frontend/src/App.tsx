import {Button, Container, Stack, Text, Title} from '@mantine/core';

function App(){

  return (
    <Container>
      <Stack>
        <Title>
          Workout Planner
        </Title>
        <Text>
          Build and manage your workout plans
        </Text>
        <Button color='pink'>
          Create Plan
        </Button>
      </Stack>      
    </Container>
  );
}


export default App;