import type { MuscleGroup } from "./MuscleGroup";

export interface Exercise {
    id: number;
    name: string;
    muscleGroup: MuscleGroup 
    instructions: string;
}