import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { MantineProvider, createTheme } from '@mantine/core';
import { RouterProvider } from 'react-router';
import '@mantine/core/styles.css';
import './styles/index.css';
import { router } from './router';

const theme = createTheme({
  primaryColor: 'pink',
});

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <MantineProvider theme={theme}>
      <RouterProvider router={router} />
    </MantineProvider>
  </StrictMode>,
);