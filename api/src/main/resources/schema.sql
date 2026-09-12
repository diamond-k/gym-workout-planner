CREATE TABLE IF NOT EXISTS exercises (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    muscle_group ENUM(
        'BACK',
        'BICEPS',
        'CHEST',
        'CORE',
        'GLUTES',
        'LEGS',
        'SHOULDERS',
        'TRICEPS'
    ) NOT NULL,
    instructions VARCHAR(1000) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_exercises_name (name)
);

CREATE TABLE IF NOT EXISTS workout_plans (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS workout_plan_exercises (
    id INT NOT NULL AUTO_INCREMENT,
    workout_plan_id INT NOT NULL,
    exercise_id INT NOT NULL,
    target_sets INT NOT NULL,
    target_reps INT NOT NULL,
    exercise_order INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_workout_plan_exercise
        UNIQUE (workout_plan_id, exercise_id),
    CONSTRAINT fk_workout_plan_exercise_plan
        FOREIGN KEY (workout_plan_id)
        REFERENCES workout_plans(id),
    CONSTRAINT fk_workout_plan_exercise_exercise
        FOREIGN KEY (exercise_id)
        REFERENCES exercises(id)
);