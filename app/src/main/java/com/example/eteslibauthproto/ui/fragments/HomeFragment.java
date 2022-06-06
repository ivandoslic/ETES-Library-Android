package com.example.eteslibauthproto.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.eteslibauthproto.R;
import com.example.eteslibauthproto.databinding.FragmentHomeBinding;
import com.example.eteslibauthproto.firestore.FirestoreClass;
import com.example.eteslibauthproto.models.Book;
import com.example.eteslibauthproto.models.User;
import com.example.eteslibauthproto.ui.activities.BookPreviewActivity;
import com.example.eteslibauthproto.ui.activities.SearchActivity;
import com.example.eteslibauthproto.ui.activities.SettingsActivity;
import com.example.eteslibauthproto.utils.Constants;
import com.example.eteslibauthproto.utils.ETESLibTextView;
import com.example.eteslibauthproto.utils.GlideLoader;
import com.example.eteslibauthproto.utils.adapters.BooksHorizontalListAdapter;
import com.example.eteslibauthproto.utils.localdatabase.AppDataManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HomeFragment extends BaseFragment {

    // private HomeViewModel homeViewModel;
    private User mUser;
    private ArrayList<Book> randomBooks;
    private ArrayList<Book> specificAuthorBooks;
    private HashMap<String, RecyclerView> loadedRecyclers;
    private HashMap<String, ArrayList<Book>> listsForRecyclers;
    private ArrayList<View> headers;
    private ArrayList<String> displayedAuthors;
    private ArrayList<String> displayedGenres;
    private FragmentHomeBinding binding;
    private AppDataManager dataManager;
    private int noOfLoads = 0;
    TextView usernameText;
    ImageView profileImageView;
    ConstraintLayout rootLayout;
    private boolean hasBeenPaused;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        FirestoreClass.getUserDetailsFragment(this);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
        profileImageView = binding.profileImageViewHome;
        usernameText = binding.usernameLabelHome;
        rootLayout = binding.homeConstraintRootLayout;
        CardView search = binding.homeFragmentSearchCard;

        search.setOnClickListener(v -> {
                Intent i = new Intent(getContext(), SearchActivity.class);
                startActivity(i);
        });

        binding.profileImageCardHome.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), SettingsActivity.class);
            startActivity(i);
        });

        return root;
    }

    private void initialLoad() {
        if(mUser != null) {
            GlideLoader.loadImageFromURLToFragment(this, mUser.getImage(), profileImageView);
            usernameText.setText(mUser.getName());
        }

        FirestoreClass.getTenBooks(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(hasBeenPaused) {
            updateUI();
            hasBeenPaused = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        hasBeenPaused = true;
        resetHomeContent();
    }

    private void updateUI() {
        if(mUser != null) {
            GlideLoader.loadImageFromURLToFragment(this, mUser.getImage(), profileImageView);
            usernameText.setText(mUser.getName());
        }

        resetHomeContent();

        View lastLoaded = null;

        for(Map.Entry<String, RecyclerView> recyclerViewEntry : loadedRecyclers.entrySet()) {
            for(Map.Entry<String, ArrayList<Book>> bookListEntry : listsForRecyclers.entrySet()) {
                if(recyclerViewEntry.getKey().compareTo(bookListEntry.getKey()) == 0) {
                    RecyclerView targetRecycler = recyclerViewEntry.getValue();
                    ArrayList<Book> targetList = bookListEntry.getValue();

                    ConstraintSet set = new ConstraintSet();
                    rootLayout.addView(targetRecycler, 0);
                    targetRecycler.setId(View.generateViewId());
                    set.clone(rootLayout);

                    lastLoaded = generateHeader(lastLoaded, recyclerViewEntry.getKey());

                    if(lastLoaded == null){
                        set.connect(targetRecycler.getId(), ConstraintSet.TOP, binding.discoverLabelHomeFragment.getId(), ConstraintSet.BOTTOM, 10);
                    } else {
                        set.connect(targetRecycler.getId(), ConstraintSet.TOP, lastLoaded.getId(), ConstraintSet.BOTTOM, 45);
                    }

                    lastLoaded = targetRecycler;

                    set.applyTo(rootLayout);

                    BooksHorizontalListAdapter booksHorizontalListAdapter = new BooksHorizontalListAdapter(getContext(), targetList);

                    booksHorizontalListAdapter.setOnItemClickListener(new BooksHorizontalListAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View itemView, int position) {
                            Intent i = new Intent(getActivity(), BookPreviewActivity.class);
                            i.putExtra(Constants.BOOK_PREVIEW_INTENT_NAME, targetList.get(position));
                            i.putExtra(Constants.EXTRA_USER_DETAILS, mUser);
                            startActivity(i);
                        }
                    });

                    targetRecycler.setAdapter(booksHorizontalListAdapter);
                    targetRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                }
            }
        }
    }

    private View generateHeader(View lastLoaded, String key) {
        String type = "basic";
        if(key.contains("|")) {
            type = key.split("\\|")[0];
        }

        View header = new View(getContext());

        if(type.compareTo("basic") == 0) {
            ETESLibTextView basicHeader = new ETESLibTextView(getContext());
            basicHeader.setText(getRandomHeaderMessage());
            basicHeader.setTypeface(ResourcesCompat.getFont(getContext(), R.font.josefin_sans));
            basicHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.home_high_emphasis));
            basicHeader.setTextSize(24);
            // add styling
            basicHeader.setId(View.generateViewId());
            header = basicHeader;
        } else if (type.compareTo("author") == 0) {
            ConstraintLayout authorHeader = new ConstraintLayout(getContext());
            authorHeader.setLayoutParams(new ConstraintLayout.LayoutParams(DrawerLayout.LayoutParams.MATCH_PARENT, 220));
            CardView authorProfileCard = new CardView(getContext());
            authorProfileCard.setLayoutParams(new RelativeLayout.LayoutParams(220, 220));
            authorProfileCard.setRadius(110);
            ImageView authorProfileImage = new ImageView(getContext());
            authorProfileCard.addView(authorProfileImage);
            authorHeader.addView(authorProfileCard);
            ETESLibTextView booksByTv = new ETESLibTextView(getContext());
            ETESLibTextView authorNameTv = new ETESLibTextView(getContext());
            booksByTv.setTypeface(ResourcesCompat.getFont(getContext(), R.font.josefin_sans));
            booksByTv.setText("BOOKS BY");

            String authorImage = listsForRecyclers.get(key).get(0).getAuthor().getImage();
            Log.d("authorImage", "generateHeader: " + authorImage);
            String authorName = formatAuthorsName(key.split("\\|")[1]);
            authorNameTv.setText(authorName);
            authorNameTv.setTypeface(ResourcesCompat.getFont(getContext(), R.font.josefin_sans));
            authorNameTv.setTextColor(ContextCompat.getColor(getContext(), R.color.home_high_emphasis));
            authorNameTv.setTextSize(28);

            Glide.with(getContext())
                    .load(authorImage)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(authorProfileImage);

            authorHeader.setId(View.generateViewId());
            authorProfileCard.setId(View.generateViewId());
            authorProfileImage.setId(View.generateViewId());
            booksByTv.setId(View.generateViewId());
            authorNameTv.setId(View.generateViewId());

            authorHeader.addView(booksByTv);
            authorHeader.addView(authorNameTv);

            ConstraintSet authorSet = new ConstraintSet();
            authorSet.clone(authorHeader);
            authorSet.connect(authorProfileCard.getId(), ConstraintSet.START, authorHeader.getId(), ConstraintSet.START);
            authorSet.connect(authorProfileCard.getId(), ConstraintSet.TOP, authorHeader.getId(), ConstraintSet.TOP);
            authorSet.connect(booksByTv.getId(), ConstraintSet.TOP, authorHeader.getId(), ConstraintSet.TOP);
            authorSet.connect(booksByTv.getId(), ConstraintSet.START, authorProfileCard.getId(), ConstraintSet.END, 50);
            authorSet.connect(authorNameTv.getId(), ConstraintSet.TOP, booksByTv.getId(), ConstraintSet.BOTTOM);
            authorSet.connect(authorNameTv.getId(), ConstraintSet.START, booksByTv.getId(), ConstraintSet.START);
            authorSet.applyTo(authorHeader);

            header = authorHeader;
        } else if (type.compareTo("genre") == 0){
            ETESLibTextView genreHeader = new ETESLibTextView(getContext());
            String genreName = key.split("\\|")[1];
            genreHeader.setText(genreName);
            genreHeader.setTypeface(ResourcesCompat.getFont(getContext(), R.font.josefin_sans));
            genreHeader.setTextColor(ContextCompat.getColor(getContext(), R.color.home_high_emphasis));
            genreHeader.setTextSize(24);
            // add styling
            genreHeader.setId(View.generateViewId());
            header = genreHeader;
        }

        ConstraintSet set = new ConstraintSet();
        rootLayout.addView(header, 0);
        set.clone(rootLayout);

        if(lastLoaded == null){
            set.connect(header.getId(), ConstraintSet.TOP, binding.discoverLabelHomeFragment.getId(), ConstraintSet.BOTTOM, 50);
        } else {
            set.connect(header.getId(), ConstraintSet.TOP, lastLoaded.getId(), ConstraintSet.BOTTOM, 85);
        }
        set.connect(header.getId(), ConstraintSet.START, rootLayout.getId(), ConstraintSet.START, 65);
        set.applyTo(rootLayout);

        if(headers == null) {
            headers = new ArrayList<>();
        }

        headers.add(header);
        return header;
    }

    private String formatAuthorsName(String s) {
        String[] parts = s.split(" ");
        String result = "";
        for(int i = 0; i < parts.length; i++){
            if(i == 0) {
                result += parts[i] + " ";
            } else {
                result += parts[i].charAt(0) + ". ";
            }
        }
        return result;
    }

    private String getRandomHeaderMessage() {
        Random random = new Random();
        switch (random.nextInt(5)) {
            case 1:
                return "Try some of these!";
            case 2:
                return "Maybe you'll like...";
            case 3:
                return "How about...";
            case 4:
                return "Recommended";
            default:
                return "Random picks...";
        }
    }

    private void resetHomeContent() {
        for(Map.Entry<String, RecyclerView> recyclerViewEntry : loadedRecyclers.entrySet()) {
            rootLayout.removeView(recyclerViewEntry.getValue());
        }
        if(headers != null && !headers.isEmpty()) {
            for(View header : headers) {
                rootLayout.removeView(header);
            }
            headers.clear();
        }
    }

    public void storeUser(User u) {
        mUser = u;
        initialLoad();
    }

    public void gotTenBooks(ArrayList<DocumentSnapshot> snaps) {
        showProgressDialog("Loading...");
        ArrayList<Book> tempBookList = new ArrayList<>();
        snaps.forEach((bookDoc) -> {
            Book tempBook = new Book(bookDoc);
            tempBookList.add(tempBook);
        });

        if(listsForRecyclers == null) {
            listsForRecyclers = new HashMap<>();
        }

        listsForRecyclers.put("basicList", tempBookList);

        RecyclerView basicBooksDisplay = new RecyclerView(getContext());

        if(loadedRecyclers == null) {
            loadedRecyclers = new HashMap<>();
        }

        loadedRecyclers.put("basicList", basicBooksDisplay);

        String randomAuthorName = getRandomAuthorName();

        FirestoreClass.getRandomAuthorBooks(this, randomAuthorName);
    }

    public void generateRandomAuthorList(String authorName, ArrayList<Book> authorsBooks) {
        RecyclerView authorRecycler = new RecyclerView(getContext());
        listsForRecyclers.put("author|"+authorName, authorsBooks);
        loadedRecyclers.put("author|"+authorName, authorRecycler);

        String randomGenre = getRandomGenre();

        if(randomGenre.compareTo("none") == 0) {
            return;
        }

        FirestoreClass.getRandomGenreBooks(this, randomGenre);

        if(displayedAuthors == null)
            displayedAuthors = new ArrayList<>();

        displayedAuthors.add(authorName);
    }

    public void generateRandomGenreList(String randomGenre, ArrayList<Book> booksOfGenre) {
        RecyclerView genreRecycler = new RecyclerView(getContext());
        listsForRecyclers.put("genre|"+randomGenre, booksOfGenre);
        loadedRecyclers.put("genre|"+randomGenre, genreRecycler);

        if(displayedGenres == null)
            displayedGenres = new ArrayList<>();

        displayedGenres.add(randomGenre);

        noOfLoads++;

        if(noOfLoads < 4) {
            String randomAuthor = getRandomAuthorName();
            FirestoreClass.getRandomAuthorBooks(this, randomAuthor);
        } else {
            resetHomeContent();
            updateUI();
            hideProgressDialog();
        }
    }

    private String getRandomGenre() {
        Random random = new Random();
        if(displayedGenres == null || displayedGenres.isEmpty()) {
            int randIdx = random.nextInt(listsForRecyclers.get("basicList").size());
            return listsForRecyclers.get("basicList").get(randIdx).getGenre();
        } else {
            ArrayList<String> filteredGenreList = new ArrayList<String>();
            for(Book book : listsForRecyclers.get("basicList")) {
                for(String item : displayedGenres) {
                    if(item.compareTo(book.getGenre()) != 0) {
                        filteredGenreList.add(book.getGenre());
                    }
                }
            }
            if(filteredGenreList.isEmpty()) {
                return "none";
            } else {
                int randIdx = random.nextInt(filteredGenreList.size());
                return filteredGenreList.get(randIdx);
            }
        }
    }

    private String getRandomAuthorName() {
        Random random = new Random();
        if(displayedAuthors == null || displayedAuthors.isEmpty()) {
            int randIdx = random.nextInt(listsForRecyclers.get("basicList").size());
            return listsForRecyclers.get("basicList").get(randIdx).getAuthorName();
        } else {
            ArrayList<String> filteredAuthorList = new ArrayList<String>();
            for(Book book : listsForRecyclers.get("basicList")) {
                for(String item : displayedAuthors) {
                    if(item.compareTo(book.getAuthorName()) != 0) {
                        filteredAuthorList.add(book.getAuthorName());
                    }
                }
            }
            if(filteredAuthorList.isEmpty()) {
                return "none";
            } else {
                int randIdx = random.nextInt(filteredAuthorList.size());
                return filteredAuthorList.get(randIdx);
            }
        }
    }
}