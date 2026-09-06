import { Avatar, Container, Group, Text } from '@mantine/core';
import { IconBarbellFilled } from '@tabler/icons-react';
import './Navbar.css';

function Navbar() {
  return (
    <header className="navbar">
      <Container size="lg" py="sm">
        <Group justify="space-between">        
          <Group gap="xs">
            <IconBarbellFilled className="iconColour" size={32} />
            <Text fw={700}>Workout Planner</Text>
          </Group>  
          <Group gap="xs">
            <Avatar classNames={{ placeholder: 'avatarColour' }} radius="xl">
                D
            </Avatar>
            <Text className="accountName">Hi, Diamond 👋</Text>
          </Group>
        </Group>
      </Container>
    </header>
  );
}

export default Navbar;