package itba.undiaparadar.services;

import android.content.Context;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.monits.volleyrequests.restsupport.Rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import itba.undiaparadar.R;
import itba.undiaparadar.model.PositiveAction;
import itba.undiaparadar.model.Topic;

public class TopicServiceImpl implements TopicService {
    @Inject
    private Gson gson;
    @Inject
    private RequestQueue requestQueue;

    @Override
    public void loadImageResId(final Topic topic, final ImageView imageView) {
        if (topic.isSelected()) {
            topic.unselect();
            imageView.setImageResource(topic.getDisableImageResId());
        } else {
            topic.select();
            imageView.setImageResource(topic.getEnableImageResId());
        }
    }

    @Override
    public List<Topic> getSelectedTopics(final Collection<Topic> topics) {
        final List<Topic> selectedTopics = new ArrayList<>();

        for (final Topic topic : topics) {
            if (topic.isSelected()) {
                selectedTopics.add(topic);
            }
        }
        return selectedTopics;
    }

    @Override
    public void getPositiveActionsForTopics(final Collection<Topic> collection, final Response.Listener<List<PositiveAction>> listener,
        final Response.ErrorListener errorListener) {
        Rest.setBaseUrl("http://search.ourmark.com/");
        Rest.setGson(gson);
        final Map<String, String> topicsQuery = new HashMap<>();
        final StringBuilder topicQueryBuilder = new StringBuilder("topics:(");
        final Topic[] topics = collection.toArray(new Topic[collection.size()]);
        for (int i = 0; i < topics.length; i++) {
            topicQueryBuilder.append(topics[i].getId());
            if (i != topics.length - 1) {
                topicQueryBuilder.append("+OR+");
            }
        }
        topicQueryBuilder.append(")");
        topicsQuery.put("q", topicQueryBuilder.toString());
        topicsQuery.put("wt", "json");
        topicsQuery.put("indent", "true");
        topicsQuery.put("rows", "10000000");

        final Request<List<PositiveAction>> request = Rest
                .one("solr", "classfield_core")
            .all("select")
            .get(PositiveAction.class)
            .query(topicsQuery)
            .onSuccess(listener)
            .onError(errorListener)
            .request();

        requestQueue.add(request);
    }

    @Override
    public HashMap<Long, Topic> createTopics(final Context context) {
        final HashMap<Long, Topic> topics = new HashMap<>();

        topics.put(new Long(14), new Topic(14, R.drawable.adopcion_de_mascotas, context.getString(R.string.adopcion_mascotas), R.drawable.adopcion_de_mascotas_gris));
        topics.put(new Long(1), new Topic(1, R.drawable.asesor_directorio, context.getString(R.string.asesor_directorio), R.drawable.asesor_directorio_gris));
        topics.put(new Long(6), new Topic(6, R.drawable.donante_de_medula_osea, context.getString(R.string.donante_de_medula_osea), R.drawable.donante_de_medula_osea_gris));
        topics.put(new Long(8), new Topic(8, R.drawable.donar_alimentos, context.getString(R.string.donar_alimentos), R.drawable.donar_alimentos_gris));
        topics.put(new Long(9), new Topic(9, R.drawable.donar_articulos_usados, context.getString(R.string.donar_articulos_usados), R.drawable.donar_articulos_usados_gris));
        topics.put(new Long(7), new Topic(7, R.drawable.donar_dinero, context.getString(R.string.donar_dinero), R.drawable.donar_dinero_gris));
        topics.put(new Long(13), new Topic(13, R.drawable.donar_leche_materna, context.getString(R.string.donar_leche_materna), R.drawable.donar_leche_materna_gris));
        topics.put(new Long(2), new Topic(2, R.drawable.donar_millas_aereas, context.getString(R.string.donar_millas_aereas), R.drawable.donar_millas_aereas_gris));
        topics.put(new Long(10), new Topic(10, R.drawable.donar_pelo, context.getString(R.string.donar_pelo), R.drawable.donar_pelo_gris));
        topics.put(new Long(16), new Topic(16, R.drawable.donar_plaquetas, context.getString(R.string.donar_plaquetas), R.drawable.donar_plaquetas_gris));
        topics.put(new Long(5), new Topic(5, R.drawable.donar_sangre, context.getString(R.string.donar_sangre), R.drawable.donar_sangre_gris));
        topics.put(new Long(15), new Topic(15, R.drawable.donar_telefonos_moviles, context.getString(R.string.donar_telefonos_moviles), R.drawable.donar_telefonos_moviles_gris));
        topics.put(new Long(21), new Topic(21, R.drawable.formarse_para_ayudar, context.getString(R.string.formarse_para_ayudar), R.drawable.formarse_para_ayudar_gris));
        topics.put(new Long(22), new Topic(22, R.drawable.plantar_arboles, context.getString(R.string.plantar_arboles), R.drawable.plantar_arboles_gris));
        topics.put(new Long(20), new Topic(20, R.drawable.publicacion_de_calle, context.getString(R.string.publicacion_de_calle), R.drawable.publicacion_de_calle_gris));
        topics.put(new Long(18), new Topic(18, R.drawable.reutilizar, context.getString(R.string.reutilizar), R.drawable.reutilizar_gris));
        topics.put(new Long(24), new Topic(24, R.drawable.voluntariado, context.getString(R.string.voluntariado), R.drawable.voluntariado_gris));
        return topics;
    }
}
