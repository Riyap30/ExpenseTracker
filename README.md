# ExpenseTrackr

## Installation and Dependencies

### Backend

To run this project, you will need the following dependencies installed:

- **JDK 17**
- **Apache Maven**
- **PostgreSQL**
- **H2 Database**

### Frontend

For the frontend, ensure you have the following installed:

- **Node.js**
- **npm**
- **Vite**
- **TypeScript**

## Cloning the Repository

Clone this repository into a folder and open it using your preferred code editor, such as **VSCode** or **IntelliJ**.

```bash
git clone <repository_url>
```
## Setup
Once you've cloned the repository, follow these steps:

- Open two terminals:

-- One in the root folder of the project.
-- Another in the frontend folder.

- In the root terminal, run:
```bash
root_directory> mvn install
```

- In the "_frontend_" folder terminal, run:
```bash
root_directory/frontend> npm install
```

## Database Configuration

After the installations are complete, update the `_application.properties_` file located in the backend's `_src/main/resources_` directory with your PostgreSQL database connection information.

```bash
# Example configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database_name
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## Running the Application

Once the setup is done, follow these steps to run the application:
- In the root terminal, run:
```bash
root_directory> mvnw spring-boot:run
```

- In the "_frontend_" folder terminal, run:
```bash
root_directory/frontend> npm run dev
```

- Access the application:
After running the frontend server, you'll receive a URL in the terminal (like _http://localhost:5173/_). Open this URL in the web browser to access the application.

## License


### Explanation:
- The code blocks that contain commands (e.g., `mvn install`, `npm install`) are formatted using triple backticks to simulate a terminal appearance.
- Each section is clearly labeled for easy navigation and understanding.
- The PostgreSQL connection example in the `application.properties` is provided as a guide for users to update their database connection settings.
- The instructions are straightforward to help users set up and run the project smoothly.

