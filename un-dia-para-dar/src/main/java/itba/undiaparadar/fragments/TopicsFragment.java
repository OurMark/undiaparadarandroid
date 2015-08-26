package itba.undiaparadar.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import itba.undiaparadar.R;
import itba.undiaparadar.adapter.TopicsItemAdapter;
import itba.undiaparadar.model.Topic;

public class TopicsFragment extends Fragment {
	private View root;

	public static Fragment newInstance() {
		return new TopicsFragment();
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

		final TopicsItemAdapter adapter = new TopicsItemAdapter(getActivity(), createTopics());

		gridView.setAdapter(adapter);


	}

	private List<Topic> createTopics() {
		final List<Topic> topics = new ArrayList<>();

		topics.add(new Topic(R.drawable.adopcion_de_mascotas));
		topics.add(new Topic(R.drawable.asesor_directorio));
		topics.add(new Topic(R.drawable.donante_de_medula_osea));
		topics.add(new Topic(R.drawable.donar_alimentos));
		topics.add(new Topic(R.drawable.donar_articulos_usados));
		topics.add(new Topic(R.drawable.donar_dinero));
		topics.add(new Topic(R.drawable.donar_leche_materna));
		topics.add(new Topic(R.drawable.donar_millas_aereas));
		topics.add(new Topic(R.drawable.donar_pelo));
		topics.add(new Topic(R.drawable.donar_plaquetas));
		topics.add(new Topic(R.drawable.donar_sangre));
		topics.add(new Topic(R.drawable.donar_telefonos_moviles));
		topics.add(new Topic(R.drawable.formarse_para_ayudar));
		topics.add(new Topic(R.drawable.plantar_arboles));
		topics.add(new Topic(R.drawable.publicacion_de_calle));
		topics.add(new Topic(R.drawable.reutilizar));
		topics.add(new Topic(R.drawable.voluntariado));

		return topics;
	}


}
