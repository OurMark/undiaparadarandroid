package itba.undiaparadar.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.inject.Inject;
import com.monits.skeletor.base.BaseStruct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import itba.undiaparadar.R;
import itba.undiaparadar.UnDiaParaDarApplication;
import itba.undiaparadar.adapter.TopicsItemAdapter;
import itba.undiaparadar.interfaces.TitleProvider;
import itba.undiaparadar.model.Topic;
import itba.undiaparadar.services.TopicService;

public class TopicsFragment extends Fragment implements TitleProvider {
    private View root;
    @Inject
    private TopicService topicService;
    private HashMap<Long, Topic> topics;

    public static Fragment newInstance() {
        return new TopicsFragment();
    }

    public TopicsFragment() {
        UnDiaParaDarApplication.injectMembers(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_topics, container, false);
        populateGrid();

        return root;
    }

    private void populateGrid() {
        final GridView gridView = (GridView) root.findViewById(R.id.topics_grid);

        final TopicsItemAdapter adapter = new TopicsItemAdapter(getActivity(), createTopics().values());

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, final View view,
                                    final int position, final long id) {
                final ImageView img = (ImageView) view.findViewById(R.id.topic_img);
                topicService.loadImageResId(adapter.getItem(position), img);
                if (isAnyTopicSelected()) {
                    setHasOptionsMenu(true);
                } else {
                    setHasOptionsMenu(false);
                }
            }
        });

        gridView.setAdapter(adapter);


    }

    private Map<Long, Topic> createTopics() {
        topics = new HashMap<>();

        topics.put(new Long(14), new Topic(14, R.drawable.adopcion_de_mascotas, getString(R.string.adopcion_mascotas), R.drawable.adopcion_de_mascotas_gris));
        topics.put(new Long(1), new Topic(1, R.drawable.asesor_directorio, getString(R.string.asesor_directorio), R.drawable.asesor_directorio_gris));
        topics.put(new Long(6), new Topic(6, R.drawable.donante_de_medula_osea, getString(R.string.donante_de_medula_osea), R.drawable.donante_de_medula_osea_gris));
        topics.put(new Long(8), new Topic(8, R.drawable.donar_alimentos, getString(R.string.donar_alimentos), R.drawable.donar_alimentos_gris));
        topics.put(new Long(9), new Topic(9, R.drawable.donar_articulos_usados, getString(R.string.donar_articulos_usados), R.drawable.donar_articulos_usados_gris));
        topics.put(new Long(7), new Topic(7, R.drawable.donar_dinero, getString(R.string.donar_dinero), R.drawable.donar_dinero_gris));
        topics.put(new Long(13), new Topic(13, R.drawable.donar_leche_materna, getString(R.string.donar_leche_materna), R.drawable.donar_leche_materna_gris));
        topics.put(new Long(2), new Topic(2, R.drawable.donar_millas_aereas, getString(R.string.donar_millas_aereas), R.drawable.donar_millas_aereas_gris));
        topics.put(new Long(10), new Topic(10, R.drawable.donar_pelo, getString(R.string.donar_pelo), R.drawable.donar_pelo_gris));
        topics.put(new Long(16), new Topic(16, R.drawable.donar_plaquetas, getString(R.string.donar_plaquetas), R.drawable.donar_plaquetas_gris));
        topics.put(new Long(5), new Topic(5, R.drawable.donar_sangre, getString(R.string.donar_sangre), R.drawable.donar_sangre_gris));
        topics.put(new Long(15), new Topic(15, R.drawable.donar_telefonos_moviles, getString(R.string.donar_telefonos_moviles), R.drawable.donar_telefonos_moviles_gris));
        topics.put(new Long(21), new Topic(21, R.drawable.formarse_para_ayudar, getString(R.string.formarse_para_ayudar), R.drawable.formarse_para_ayudar_gris));
        topics.put(new Long(22), new Topic(22, R.drawable.plantar_arboles, getString(R.string.plantar_arboles), R.drawable.plantar_arboles_gris));
        topics.put(new Long(20), new Topic(20, R.drawable.publicacion_de_calle, getString(R.string.publicacion_de_calle), R.drawable.publicacion_de_calle_gris));
        topics.put(new Long(18), new Topic(18, R.drawable.reutilizar, getString(R.string.reutilizar), R.drawable.reutilizar_gris));
        topics.put(new Long(24), new Topic(24, R.drawable.voluntariado, getString(R.string.voluntariado), R.drawable.voluntariado_gris));

        return topics;
    }


    @Override
    public String getTitle() {
        return getString(R.string.find_what_to_give);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.go_to_map) {
            final List<Topic> selectedTopics = topicService.getSelectedTopics(topics.values());
            if (!selectedTopics.isEmpty()) {
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .replace(R.id.main_content, MapFragment.newInstance(topics))
                        .commit();
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_topic, menu);
    }

    private boolean isAnyTopicSelected() {
        for (final Topic topic : topics.values()) {
            if (topic.isSelected()) {
                return true;
            }
        }
        return false;
    }
}
