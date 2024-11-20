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
            "What is the capital of France?",
            "Which planet is known as the Red Planet?",
            "What is the largest ocean on Earth?"
    };

    private String[][] options = {
            {"Paris", "London", "Rome", "Berlin"},
            {"Earth", "Mars", "Jupiter", "Venus"},
            {"Atlantic", "Indian", "Pacific", "Arctic"}
    };

    private int[] correctAnswers = {0, 1, 2};

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

        if (correctAnswerCount > questions.length / 2) {
            binding.partyPopperImage.setVisibility(View.VISIBLE);
        } else {
            binding.partyPopperImage.setVisibility(View.GONE);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
