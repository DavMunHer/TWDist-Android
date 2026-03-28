---
description: Run Android CI Checks
---

# Run Android CI Checks

Before finishing any task or marking it as complete, you MUST run this workflow to ensure the project's CI checks (linting, building, and unit tests) pass.

// turbo-all

1. Run the lint check
Run the following command to execute the gradle lint task:
```bash
./gradlew lint
```

2. Assemble the debug build
Run the following command to ensure the debug version builds correctly:
```bash
./gradlew assembleDebug
```

3. Run the unit tests
Run the following command to execute the project's unit tests:
```bash
./gradlew test
```

If any of these commands fail, you must pause, fix the errors, and re-run all the checks before considering your work complete.
