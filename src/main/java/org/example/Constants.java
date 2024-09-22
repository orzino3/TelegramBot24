package org.example;

public class Constants
{
    public static final String BOT_USER_NAME = "SadnaAsh2024bot";
    public static final String BOT_TOKEN = "7041111605:AAHfbokQMt3483BqaQ8HCR4TCf_YVV0z-bQ";
    public static final String OPTION_1 = "/start";
    public static final String OPTION_2 = "Hi";
    public static final String OPTION_3 = "היי";
    public static final String CREATE = "/create_survey";
    public static final String FINISH = "/finish";
    public static final int MIN_USER_LIMIT = 3;
    public static final int MIN_ANSWER = 2;
    public static final int MAX_ANSWER = 4;
    public static final int MAX_QUESTION_NUM = 3;
    public static final int MAX_ANSWER_TIME = 5;
    public static final String MESSAGE_DEFAULT = "בבקשה השתמש ב'/start', 'היי', או 'hi' כדי להצטרף לקהילה. השתמש ב'/create_survery' על מנת ליצור סקר, השתמש ב'/finish' על מנת לסיים את יצירת הסקר.";
    public static final String MESSAGE_JOINED = " הצטרף לקהילה! כמות משתמשים: ";
    public static final String MESSAGE_ALREADY_JOINED = "אתה כבר חלק מהקהילה.";

    public static final String MESSAGE_INSTRUCTION = """
                ברוכים הבאים לבוט הסקרים! אלו ההוראות לשימוש הבוט:
                1. /create_survey - יצירת סקר ע"י שליחה של שאלות ותשובות.
                2. אחרי כל שאלה, ספק לבוט בין 2 ל-4 תשובות אפשריות המופרדות ע"י פסיק.
                3. /finish - סיום יצירת הסקר ושליחתו למשתמשים הרשומים.
                4. ענה לכל שאלה בסקר עם מס' האפשרות של אותה תשובה רלוונטית.

                בהצלחה!
                """;

    public static final String MESSAGE_SURVEY_ACTIVE = "יש כבר סקר פעיל. אנא המתן לסיומו.";
    public static final String MESSAGE_SURVEY_CREATION_USER_LIMIT_ERROR_FIRST = "על מנת ליצור סקר, חובה שיהיו ";
    public static final String MESSAGE_SURVEY_CREATION_USER_LIMIT_ERROR_SECOND = " משתמשים רשומים.";
    public static final String MESSAGE_SURVEY_CREATION_QUESTIONS_LIMIT_ERROR = "הגעת לכמות השאלות המקסימלית עבור הסקר. אנא השתמש בפקודה '/finish' על מנת להשלים את הסקר";
    public static final String MESSAGE_SURVEY_CREATION_1 = "אנא שלח את השאלה הראשונה לסקר.";
    public static final String MESSAGE_SURVEY_CREATION_2 = "בבקשה ספק בין 2 ל-4 תשובות אפשריות. הפרד את התשובות באמצעות פסיק.";
    public static final String MESSAGE_SURVEY_CREATION_2_ERROR = "כמות אפשרויות אינה תקינה. אנא ספק בין 2 ל-4 תשובות אפשריות. הפרד את התשובות באמצעות פסיק.";
    public static final String MESSAGE_SURVEY_CREATION_1_SUCCESS = "שאלה נוספה בהצלחה. יש אפשרות לשלוח שאלה נוספת, או הקלד '/finish' על מנת להשלים את יצירת הסקר. במידה ותגיע למגבלת כמות השאלות, תתבקש לסיים את יצירת הסקר.";
    public static final String MESSAGE_SURVEY_CREATION_ANSWER_ADD = "אנא ספק בין 2 ל-4 תשובות אפשרויות עבור השאלה הנוספת, והפרד בין התשובות באמצעות פסיק.";
    public static final String MESSAGE_SURVEY_NO_ACTIVE = "אין סקר פעיל. אנא צור סקר באמצעות '/create_survey'.";
    public static final String MESSAGE_SURVEY_DELAY_SET = "סקר נוצר בהצלחה. האם תרצה לשלוח את הסקר באופן מיידי או לאחר השהייה מסוימת? הקלד 'מיידי' או ספק את מס' הדקות להשהייה (בדקות).";
    public static final String MESSAGE_SURVEY_DELAY_IMMEDIATELY = "מיידי";
    public static final String MESSAGE_SURVEY_DELAY_IMMEDIATELY_SEND = "שולח סקר מיד";
    public static final String MESSAGE_SURVEY_DELAY_INVALID = "זמן השהיה לא תקין. הסקר ישלח באופן מיידי.";
    public static final String MESSAGE_SURVEY_DELAY_FIRST_PART = "הסקר ישלח לאחר ";
    public static final String MESSAGE_SURVEY_DELAY_SECOND_PART = " דקות.";
    public static final String MESSAGE_SURVEY_ANSWER_NOT_VALID = "תשובה לא תקינה. אנא בחר אפשרות תקינה.";
    public static final String MESSAGE_SURVEY_ANSWER_THANKS = "תודה רבה על המענה!.";
    public static final String MESSAGE_SURVEY_COMPLETE = "השלמת את המענה על הסקר. תודה רבה!";
    public static final String MESSAGE_SURVEY_RESULT = "תוצאות הסקר: \n";
    public static final String MESSAGE_SURVEY_QUESTION = "שאלה: ";
    public static final String MESSAGE_SURVEY_NO_RESPONSE ="אין מענה על השאלה\n\n";
    public static final String MESSAGE_SURVEY_VOTE_TEXT_1 = " קול";
    public static final String MESSAGE_SURVEY_VOTE_TEXT_MULTIPLE = "ות";


}

