package com.ai_tutor.Utilities;

public class PromptTemplates {

    public static final String DEFAULT_QUESTION_PROMPT = """
        Create a JSON object representing a multiple choice investing-related question.
        The format should be:
        {
          "question": "Your question here?",
          "answers": [
            {"option": "Option A", "cost": 100.0, "explanation": "Explanation for Option A"},
            {"option": "Option B", "cost": 50.0, "explanation": "Explanation for Option B"},
            {"option": "Option C", "cost": -20.0, "explanation": "Explanation for Option C"},
            {"option": "Option D", "cost": -50.0, "explanation": "Explanation for Option D"}
          ],
          "correctAnswer": {"option": "Option A", "cost": 100.0, "explanation": "Explanation for Option A"}
        }
        Make the question relevant to stock investing or portfolio decision-making for a beginner.
        """;

    public static final String STOCK_SPECIFIC_PROMPT = """
        Create a JSON object representing a multiple choice investing-related question specifically about %s stock.
        The format should be:
        {
          "question": "Your question here about %s?",
          "answers": [
            {"option": "Option A", "cost": 100.0, "explanation": "Explanation for Option A"},
            {"option": "Option B", "cost": 50.0, "explanation": "Explanation for Option B"},
            {"option": "Option C", "cost": -20.0, "explanation": "Explanation for Option C"},
            {"option": "Option D", "cost": -50.0, "explanation": "Explanation for Option D"}
          ],
          "correctAnswer": {"option": "Option A", "cost": 100.0, "explanation": "Explanation for Option A"}
        }
        Make the question relevant to %s investing, recent developments, or typical investment strategies related to this company.
        """;

    public static final String CUSTOM_SCENARIO_PROMPT = """
        Create a JSON object representing a multiple choice investing-related question about this scenario: "%s".
        The format should be:
        {
          "question": "Your question here related to the scenario?",
          "answers": [
            {"option": "Option A", "cost": 100.0, "explanation": "Explanation for Option A"},
            {"option": "Option B", "cost": 50.0, "explanation": "Explanation for Option B"},
            {"option": "Option C", "cost": -20.0, "explanation": "Explanation for Option C"},
            {"option": "Option D", "cost": -50.0, "explanation": "Explanation for Option D"}
          ],
          "correctAnswer": {"option": "Option A", "cost": 100.0, "explanation": "Explanation for Option A"}
        }
        Make sure the question challenges the user to think about how to handle this specific investing scenario.
        """;

    public static final String GPT_REQUEST_TEMPLATE = """
        {
            "model": "gpt-3.5-turbo",
            "messages": [
                {"role": "system", "content": "You are an investing tutor with up-to-date knowledge about financial markets and investment strategies."},
                {"role": "user", "content": "%s"}
            ],
            "temperature": 0.7
        }
        """;

    public static final String RANDOM_SCENARIO_GENERATOR_PROMPT =
            "Generate a brief, realistic investing scenario (1-2 sentences only) that could be used to create an educational question for beginner investors. " +
                    "Focus on topics like market trends, stock evaluation, portfolio diversification, or financial news events. " +
                    "Return only the scenario text with no additional formatting or explanation.";
}
