# GitHub Copilot Instructions

Before finishing any task, completing any work, or returning final results, you MUST run the project's CI checks to ensure all GitHub Actions workflows pass.

Please run the following Gradle commands in the root of the project:
1. `./gradlew lint`
2. `./gradlew assembleDebug`
3. `./gradlew test`

Only consider your work complete if all of these checks complete successfully without errors. Ensure that any code changes do not break linting, building, or unit tests. If any check fails, fix the code and run the checks again before completing the task.
