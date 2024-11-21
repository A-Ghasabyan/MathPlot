package com.example.myapplication.ui.quiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.databinding.FragmentQuizBinding;

public class QuizFragment extends Fragment {

    private FragmentQuizBinding binding;
    private int currentQuestionIndex = 0;
    private int correctAnswerCount = 0;

    private String[] questions = {
            "Ինչ է Գաուսի գլխարկը և ինչպե՞ս է այն կիրառվում գիտության մեջ?", // Gaussian bell curve and its scientific use
            "Ինչ է Գաուսի բաշխումը և ինչպե՞ս է այն օգտագործվում տվյալագիտության մեջ?", // Gaussian distribution in data science
            "Ինչպես է կատալազ ֆերմենտը գործում?", // How catalase enzyme works
            "Ինչ է 68-95-99.7 օրենքը և ինչպե՞ս է այն աշխատում?", // 68-95-99.7 rule (standard deviation)
            "Ինչ է Միքայելիս-Մենտենի հավասարումը նկարագրում?", // Michaelis-Menten equation
            "Ինչպիսի կիրառումներ ունի Գաուսի բաշխումը բժշկության ոլորտում?", // Applications of Gaussian distribution in medicine
            "Ինչպես է ցուցչային ֆունկցիան օգտագործվում ֆիզիկայում?", // Exponential function in physics
            "Ինչպես է ցուցչային ֆունկցիան կիրառվում միջուկային ֆիզիկայում?", // Exponential function in nuclear physics
            "Ինչպես է ցուցչային ֆունկցիան օգտագործվում աստղագիտության մեջ?", // Exponential function in astronomy
            "Ինչ է Ֆիբոնաչիի հաջորդականությունը և ինչպե՞ս է այն կիրառվում?", // Fibonacci sequence and its applications
    };

    private String[][] options = {
            {"Բաշխվում է ավելի մեծ չափերի վրա", "Նկարագրում է նորմալ բաշխման հավանականությունը", "Կատալիզատորները բարելավում են ֆերմենտի կայունությունը", "Ոչ մի կապ չունի գիտության հետ"}, // Question 1: Gaussian bell curve
            {"Պատմում է արժեքների բաշխումը հետազոտություններում", "Երբեմն հանդես է գալիս մեկիս դեմ", "Տարբեր բնական և սոցիալական երևույթներ մոդելավորելու համար", "Մոդելավորում է բրաունի շարժումը"}, // Question 2: Gaussian distribution
            {"Կատալազը խթանում է ջրի մոլեկուլների բեկումը", "Կատալազը խթանում է ազոտի փոխարկումը", "Կատալազը օքսիդացնում է հիդրոգենա", "Կատալազը վերամշակում է օրգանիկական թափոնները"}, // Question 3: Catalase enzyme
            {"Մոտ 95% արժեքները ընկնում են միջինի ±2σ-ի սահմաններում", "Մոտ 95% արժեքները ընկնում են միջինի ±3σ-ի սահմաններում", "Մոտ 99.7% արժեքները ընկնում են միջինի ±2σ-ի սահմաններում", "Մոտ 68% արժեքները ընկնում են միջինի ±1σ-ի սահմաններում"}, // Question 4: 68-95-99.7 rule
            {"Մոդելավորում է սուբստրատի կլանումը էնկմենթային ֆերմենտներով", "Նկարագրում է բիոկիմիական ռեակցիաների արագությունը", "Կատալիզատորները բարելավում են ֆերմենտի կայունությունը", "Ոչ մի կապ չունի կենսաքիմիայի հետ"}, // Question 5: Michaelis-Menten equation
            {"Օգտագործվում է հիվանդությունների տարածման մոդելավորման համար", "Փոխում է օրգանիզմի շուկայական վիճակները", "Հաշվարկում է մարմնի կորելապիա", "Կիրառվում է միայն տեխնիկական սարքերով"}, // Question 6: Applications of Gaussian in medicine
            {"Բացատրում է արտահայտությունների ծայրահեղությունը", "Ըստ ժամանակի բացատրությունների ցուցադրությունը", "Հասկացվում է խառը հատկություններով", "Որպես կոշտ երաշխիք կարող է նաև փոխել"}, // Question 7: Exponential function in physics
            {"Փոխհարաբերական հարաբերակցություններում թույլ չի տալիս", "Օգտագործվում է առանց ժամանակի գծի", "Այն խթանում է արագ ճեղքումը", "Ինտեգրում է ջերմաստիճանային սխալ"}, // Question 8: Exponential function in nuclear physics
            {"Հեռանկարում այն բերում է արտահայտության աճը", "Երբ անհրաժեշտ է որոշել, թե արդյոք տվյալ կետը կամ օբյեկտը համապատասխանում է որոշակի պայմաններին, օրինակ՝ աստղաբառարանից գոյություն ունեցող օբյեկտներին", "Ընդգրկում է միայն ճառագայթները", "Կախված է աստղի հզորությունից"}, // Question 9: Exponential function in astronomy
            {"f(n) = f(n-1) + f(n-2)", "f(n) = n²", "f(n) = 2^n", "f(n) = n!"}, // Question 10: Fibonacci sequence
    };

    private int[] correctAnswers = {1, 2, 1, 0, 1, 0, 1, 3, 1, 0}; // Correct answers updated based on new question order

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentQuizBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setupStartPage();
        return root;
    }

    private void setupStartPage() {
        binding.startPage.setVisibility(View.VISIBLE);
        binding.quizPage.setVisibility(View.GONE);
        binding.resultPage.setVisibility(View.GONE);

        binding.startQuizButton.setOnClickListener(v -> {
            binding.startPage.setVisibility(View.GONE);
            binding.quizPage.setVisibility(View.VISIBLE);
            setupQuiz();
        });
    }

    private void setupQuiz() {
        currentQuestionIndex = 0;
        correctAnswerCount = 0;
        updateQuestion();

        binding.nextButton.setOnClickListener(v -> {
            int selectedId = binding.optionsGroup.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(getActivity(), "Please select an answer!", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selectedOption = binding.getRoot().findViewById(selectedId);
            int answerIndex = binding.optionsGroup.indexOfChild(selectedOption);
            if (answerIndex == correctAnswers[currentQuestionIndex]) {
                correctAnswerCount++;
            }

            if (currentQuestionIndex < questions.length - 1) {
                currentQuestionIndex++;
                updateQuestion();
            } else {
                showResultPage();
            }
        });
    }

    private void updateQuestion() {
        binding.questionText.setText(questions[currentQuestionIndex]);
        binding.optionsGroup.removeAllViews();

        for (String option : options[currentQuestionIndex]) {
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setText(option);
            radioButton.setPadding(50, 0, 0, 0);
            binding.optionsGroup.addView(radioButton);
        }
        binding.optionsGroup.clearCheck();
    }

    private void showResultPage() {
        binding.quizPage.setVisibility(View.GONE);
        binding.resultPage.setVisibility(View.VISIBLE);
        binding.resultText.setText("You answered " + correctAnswerCount + " out of " + questions.length + " correctly!");

        // Show the party popper every time
        binding.partyPopperImage.setVisibility(View.VISIBLE);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
