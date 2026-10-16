package org.thawraty.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import android.text.InputType;
import android.graphics.drawable.GradientDrawable;

public class MainActivity extends Activity {
    private final int navy = Color.rgb(18, 39, 66);
    private final int teal = Color.rgb(0, 126, 120);
    private final int pale = Color.rgb(244, 247, 250);
    private String selectedCountry = "مصر";
    private String selectedVote = "";
    private EditText questionInput;
    private LinearLayout root;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
         showHome();
    }

    private void setup(String title, String subtitle) {
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(22), dp(20), dp(22), dp(20));
        root.setBackgroundColor(pale);
        ScrollView scroll = new ScrollView(this);
        scroll.addView(root);
        setContentView(scroll);

        TextView brand = new TextView(this);
        brand.setText("ثــورتي");
        brand.setTextSize(30);
        brand.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        brand.setTextColor(navy);
        brand.setGravity(Gravity.CENTER);
        root.addView(brand, matchWrap());

        TextView heading = new TextView(this);
        heading.setText(title);
        heading.setTextSize(23);
        heading.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        heading.setTextColor(navy);
        heading.setGravity(Gravity.RIGHT);
        heading.setPadding(0, dp(22), 0, dp(6));
        root.addView(heading, matchWrap());

        TextView sub = new TextView(this);
        sub.setText(subtitle);
        sub.setTextSize(15);
        sub.setTextColor(Color.DKGRAY);
        sub.setGravity(Gravity.RIGHT);
        sub.setPadding(0, 0, 0, dp(16));
        root.addView(sub, matchWrap());
    }

    private void showHome() {
        setup("صوتك.. اختيارك", "نموذج أولي لتجربة التصويت المجتمعي — لا يُجرى تصويت رسمي في هذه النسخة.");
        addLabel("1. اختر الدولة");
        Spinner countries = new Spinner(this);
        String[] items = {"مصر", "السعودية", "الإمارات", "الأردن", "المغرب", "الولايات المتحدة", "المملكة المتحدة", "دولة أخرى"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        countries.setAdapter(adapter);
        countries.setSelection(java.util.Arrays.asList(items).indexOf(selectedCountry) >= 0 ?
                java.util.Arrays.asList(items).indexOf(selectedCountry) : 0);
        countries.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            public void onItemSelected(android.widget.AdapterView<?> p, View v, int pos, long id) { selectedCountry = items[pos]; }
            public void onNothingSelected(android.widget.AdapterView<?> p) {}
        });
        root.addView(countries, matchWrap());

        addLabel("2. موضوع التصويت");
        questionInput = new EditText(this);
        questionInput.setHint("اكتب سؤالًا واضحًا ومحايدًا");
        questionInput.setTextSize(16);
        questionInput.setGravity(Gravity.RIGHT | Gravity.TOP);
        questionInput.setMinLines(2);
        questionInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        root.addView(questionInput, matchWrap());

        addLabel("3. اختر نوع المشاركة");
        addButton("اختيار / تأييد", () -> { selectedVote = "اختيار / تأييد"; showConfirm(); });
        addButton("عزل / رفض", () -> { selectedVote = "عزل / رفض"; showConfirm(); });

        TextView note = new TextView(this);
        note.setText("الخصوصية أولًا: لا تجمع هذه النسخة رقمًا قوميًّا أو صورة بطاقة أو شهادة ميلاد. يجب اعتماد مزود تحقق قانوني وآمن قبل أي إطلاق حقيقي.");
        note.setTextSize(13);
        note.setTextColor(Color.rgb(85, 85, 85));
        note.setGravity(Gravity.RIGHT);
        note.setPadding(0, dp(20), 0, dp(12));
        root.addView(note, matchWrap());
    }

    private void showConfirm() {
        String q = questionInput == null ? "" : questionInput.getText().toString().trim();
        if (q.isEmpty()) {
            new AlertDialog.Builder(this).setTitle("أكمل السؤال")
                .setMessage("اكتب موضوع التصويت أولًا قبل المتابعة.")
                .setPositiveButton("حسنًا", null).show();
            return;
        }
        setup("مراجعة المشاركة", "راجع البيانات قبل إرسالها. لا يتم إرسال أي بيانات إلى خادم في هذه النسخة التجريبية.");
        addLabel("الدولة");
        addValue(selectedCountry);
        addLabel("موضوع التصويت");
        addValue(q);
        addLabel("اختيارك");
        addValue(selectedVote);
        addButton("تأكيد تجريبي", () -> {
            setup("تم تسجيل الاختيار التجريبي", "هذه شاشة توضيحية فقط؛ لم يُحفظ التصويت على خادم ولم يُحتسب ضمن أي نتيجة.");
            addValue("الدولة: " + selectedCountry);
            addValue("الاختيار: " + selectedVote);
            addButton("العودة للرئيسية", this::showHome);
        });
        addButton("تعديل الاختيار", this::showHome);
    }

    private void addLabel(String text) {
        TextView v = new TextView(this);
        v.setText(text);
        v.setTextSize(16);
        v.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        v.setTextColor(navy);
        v.setGravity(Gravity.RIGHT);
        v.setPadding(0, dp(14), 0, dp(7));
        root.addView(v, matchWrap());
    }

    private void addValue(String text) {
        TextView v = new TextView(this);
        v.setText(text);
        v.setTextSize(17);
        v.setTextColor(Color.DKGRAY);
        v.setGravity(Gravity.RIGHT);
        v.setPadding(dp(14), dp(12), dp(14), dp(12));
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(Color.WHITE);
        bg.setCornerRadius(dp(10));
        bg.setStroke(dp(1), Color.rgb(220, 226, 232));
        v.setBackground(bg);
        root.addView(v, matchWrap());
    }

    private void addButton(String text, Runnable action) {
        Button b = new Button(this);
        b.setText(text);
        b.setTextSize(16);
        b.setAllCaps(false);
        b.setTextColor(Color.WHITE);
        b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(teal));
        LinearLayout.LayoutParams lp = matchWrap();
        lp.topMargin = dp(10);
        root.addView(b, lp);
        b.setOnClickListener(v -> action.run());
    }

    private LinearLayout.LayoutParams matchWrap() {
        return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }
    private int dp(int x) { return (int)(x * getResources().getDisplayMetrics().density + 0.5f); }
}
