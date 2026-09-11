import { Image } from '@mantine/core';
import { exerciseImages } from '../data/exerciseImages';

interface ExerciseImageProps {
  exerciseName: string;
}

function ExerciseImage({ exerciseName }: ExerciseImageProps) {
  const imageSrc = exerciseImages[exerciseName];

  if (!imageSrc) {
    return null;
  }

  return (
    <Image
      src={imageSrc}
      alt={exerciseName}
      w={80}
      h={100}
      radius="md"
      fit="cover"
      style={{ flexShrink: 0 }}
    />
  );
}

export default ExerciseImage;