
# SplitwiseAI

SplitwiseAI is an AI-driven expense tracking system that enhances traditional expense-tracking applications by incorporating features such as voice-based expense entry and AI-driven automatic categorization. It provides users with group-based tracking, global and group-specific category management, and advanced automation capabilities using OpenAI's Whisper and ChatGPT models.

## Features

- **Global and Group-Specific Categories**: Manage predefined global categories and create custom group-specific categories for personalized expense tracking.

- **Group and User Management**: Create groups, manage members, and assign roles with specific permissions.

- **Expense Entry and Tracking**: Add expenses with details like amount, description, and category; split payments among group members; and track balances.

- **Voice-Based Expense Entry**: Utilize OpenAI’s Whisper API to convert voice input into text for hands-free expense entry.

- **AI-Powered Category Inference**: Leverage OpenAI's ChatGPT to automatically categorize expenses based on descriptions, reducing manual effort.

## Installation and Setup

### Backend

#### Prerequisites

- **JDK 17**
- **Apache Maven**
- **PostgreSQL** (for production)
- **H2 Database** (for testing)

#### Steps

1. **Clone the Repository**:
   ```bash
   git clone <repository_url>
   ```

2. **Navigate to the Project Directory**:
   ```bash
   cd SplitwiseAI
   ```

3. **Install Dependencies**:
   ```bash
   mvn install
   ```

4. **Configure the Database**:
   Update the `application.properties` file in `src/main/resources/` with your PostgreSQL credentials:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/your_database_name
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

5. **Run the Application**:
   ```bash
   mvn spring-boot:run
   ```

### Frontend

#### Prerequisites

- **Node.js**
- **npm**
- **Vite**
- **TypeScript**

#### Steps

1. **Navigate to the Frontend Directory**:
   ```bash
   cd frontend
   ```

2. **Install Dependencies**:
   ```bash
   npm install
   ```

3. **Run the Development Server**:
   ```bash
   npm run dev
   ```
   Access the application at the URL provided in the terminal, typically `http://localhost:5173/`.

## Usage

1. **Register and Log In**: Create a new account or log in with existing credentials.

2. **Create Groups**: Set up groups for different expense-sharing scenarios (e.g., roommates, trips).

3. **Manage Categories**: Use global categories or create group-specific ones to organize expenses.

4. **Add Expenses**: Enter expenses manually or use the voice-based feature for hands-free entry.

5. **Automatic Categorization**: Leverage AI to automatically categorize expenses based on descriptions.

6. **Track Balances**: Monitor individual and group expenses, and manage settlements.

## Contributing

Contributions are welcome! Please fork the repository and create a pull request with your changes.

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.
