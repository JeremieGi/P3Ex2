package com.openclassrooms.myrepo.ui;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.openclassrooms.myrepo.R;
import com.openclassrooms.myrepo.model.Task;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Un adaptateur pour afficher la liste de tâches dans un RecyclerView.
 */
public class TaskRecyclerViewAdapter extends ListAdapter<Task, TaskRecyclerViewAdapter.ViewHolder> {

    /**
     * Constructeur de l'adaptateur.
     */
    public TaskRecyclerViewAdapter() {
        super(new ItemCallback());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    /**
     * ViewHolder pour afficher les éléments de la liste de tâches.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView factTextView;
        private final TextView dueTimeTextView;
        private final LinearProgressIndicator progressIndicator;


        /**
         * Constructeur du ViewHolder.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            factTextView = itemView.findViewById(R.id.task_description);
            dueTimeTextView = itemView.findViewById(R.id.task_duetime);
            progressIndicator = itemView.findViewById(R.id.progress_horizontal);
        }

        /**
         * Lie les données de la tâche au ViewHolder.
         *
         * @param task La tâche à afficher.
         */
        public void bind(Task task) {

            factTextView.setText(task.getDescription());

            dueTimeTextView.setText(task.getDueTimeString());

            progressIndicator.setProgress(nGetProgressIndicator_Solution(task.getDueTime()));
        }

        /**
         * Solution perso
         * @return pourcentage de temps restants sur 10 jours
         */
        private int nGetProgressIndicator(Date dDueTime) {

            int nResult; // Valeur de la progress bar de 0 à 100

            /*
            Nous voulons que la ProgressIndicator remplisse sa barre en fonction du nombre de jours restants.
            Par exemple, si une tâche a une date limite dans 3 jours, la barre doit être remplie à 70 % (car elle a été créée il y a 7 jours).
             */

            int nTotal = 10;

            Calendar calendar = Calendar.getInstance();
            Date dToday = calendar.getTime();

            // Calcul du nombre de jours entre les deux dates
            long lDifferenceEnMillis = dDueTime.getTime() - dToday.getTime();
            long lNbJoursEntre = TimeUnit.DAYS.convert(lDifferenceEnMillis, TimeUnit.MILLISECONDS);
            int nNbJoursEntre = (int)  Math.round(lNbJoursEntre);

            // Ma solution donne toujours un jour de décalage car par exemple pour le jour J : lDifferenceEnMillis = -204
            // dDueTime < dToday car dToday est initialisée après
            // La solution du cours permet de neutraliser ce décalage avec .set(Calendar.MILLISECOND, 0);

            nResult = (nTotal - nNbJoursEntre) * 10; // *10 pour renvoyer un nombre entre 0 et 100

            return nResult;
        }

        /**
         * pourcentage de temps restants sur 10 jours
         * @param dDueTime : Date de fin
         * @return
         */
        private int nGetProgressIndicator_Solution(Date dDueTime) {

            Calendar calendarToday = Calendar.getInstance();
            calendarToday.set(Calendar.HOUR_OF_DAY, 0);
            calendarToday.set(Calendar.MINUTE, 0);
            calendarToday.set(Calendar.SECOND, 0);
            calendarToday.set(Calendar.MILLISECOND, 0);

            Calendar calendarDueTime = Calendar.getInstance();
            calendarDueTime.setTime(dDueTime);
            calendarDueTime.set(Calendar.HOUR_OF_DAY, 0);
            calendarDueTime.set(Calendar.MINUTE, 0);
            calendarDueTime.set(Calendar.SECOND, 0);
            calendarDueTime.set(Calendar.MILLISECOND, 0);

            int daysDifference = (int) ((calendarDueTime.getTimeInMillis() - calendarToday.getTimeInMillis()) / (24 * 60 * 60 * 1000));

            return 100 - (daysDifference * 10);
        }
    }

    /**
     * Callback pour la comparaison des éléments de la liste.
     */
    private static class ItemCallback extends DiffUtil.ItemCallback<Task> {
        @Override
        public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.getDescription().equals(newItem.getDescription());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.equals(newItem);
        }
    }
}
