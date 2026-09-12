import { Link } from 'react-router';
import { Avatar, Container, Group, Text } from '@mantine/core';
import { IconBarbellFilled } from '@tabler/icons-react';
import '../styles/Navbar.css';

function Navbar() {
  return (
    <header className="navbar">
      <Container size="lg" py="sm">
        <Group justify="space-between">        
          <Link to="/" className="brandLink">
            <Group gap="xs">
              <IconBarbellFilled className="iconColour" size={32} />
              <Text fw={700}>Workout Planner</Text>
            </Group>
          </Link>
          <Group gap="xs">
           {/* Static placeholder user - authentication is not implemented. */}
            <Avatar classNames={{ placeholder: 'avatarColour' }} radius="xl">
              A
            </Avatar>
            <Text className="accountName">Hi, Alex 👋</Text>
          </Group>
        </Group>
      </Container>
    </header>
  );
}

export default Navbar;