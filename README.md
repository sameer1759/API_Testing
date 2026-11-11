# API Testing with Playwright (Java) and TestNG

End-to-end API test for the **EventHub** application. The test runs a complete event lifecycle through REST API calls and validates each step.

## Test Flow

1. **Login**: authenticate and extract the auth token
2. **Create Event**: create a new event and capture its ID
3. **Get Events**: fetch the events list (page 1, limit 12)
4. **Delete Event**: delete the event created in step 2
5. **Verify Deletion**: fetch the list again and confirm the deleted event ID is no longer present

## Tech Stack

- Java
- Playwright (APIRequestContext)
- TestNG
- JsonPath
- Maven

## Project Structure

```
src/test/java/APItest.java   # E2E API test
pom.xml                      # Maven dependencies
```

## How to Run

1. Clone the repository
   ```bash
   git clone https://github.com/sameer1759/API_Testing.git
   ```
2. Open the project in IntelliJ IDEA (or any Java IDE)
3. In `APItest.java`, the login payload uses the placeholders `your_email` and `your_password`. Replace them with your own **registered EventHub email and password** before running the test
4. Run the test:
   - From the IDE: right-click `APItest` → **Run**
   - Or from the terminal:
     ```bash
     mvn test
     ```

## Test Result

![Test Result](screenshots/test-result.png)

## Author

**Sameer**
