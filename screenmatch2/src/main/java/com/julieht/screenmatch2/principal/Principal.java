package com.julieht.screenmatch2.principal;

import com.julieht.screenmatch2.modelo.*;
import com.julieht.screenmatch2.repository.ISerieRepository;
import com.julieht.screenmatch2.service.ConsumoAPI;
import com.julieht.screenmatch2.service.ConvierteDatos;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private final String URL = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=82ddc3cd";
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<DatosSerie> datosSeries = new ArrayList<>();
    private ISerieRepository repository;
    private List<Serie> series;
    private Optional<Serie> serieBuscada;

    public Principal(ISerieRepository repository) {
        this.repository = repository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    **********************************************************
                    1 - Buscar Series
                    2 - Buscar Episodios de una Serie
                    3 - Mostrar Series Buscadas
                    4 - Buscar Series por Título (Nombre)
                    5 - Top 5 de Mejores Series
                    6 - Buscar Series por Categoría
                    7 - Filtrar Series por Temporadas y Evaluación
                    8 - Buscar Episodios por Título (Nombre)
                    9 - Top 5 de Episodios por Serie
                    
                    0 - Salir
                    **********************************************************
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriesPorTitulo();
                    break;
                case 5:
                    buscarTop5Series();
                    break;
                case 6:
                    buscarSeriesPorCategoria();
                    break;
                case 7:
                    filtrarSeriesPorTemporadaYEvaluacion();
                    break;
                case 8:
                    buscarEpisodiosPorTitulo();
                    break;
                case 9:
                    buscarTop5Episodios();
                    break;
                case 0:
                    System.out.println("Aplicación Cerrada!!");
                    break;
                default:
                    System.out.println("Opción no valida!!");
            }
        }
    }

    private DatosSerie getDatosSerie() {
        System.out.println("Ingrese el nombre de la Serie que quiere buscar...");
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL + nombreSerie.replace(" ", "+") + API_KEY);
        DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);
        System.out.println(datos.sinopsis());
        return datos;
    }

    private void buscarSerieWeb() {
        DatosSerie datos = getDatosSerie();
        Serie serie = new Serie(datos);
        repository.save(serie);
        //datosSeries.add(datos);
        System.out.println(datos);
    }

    private void buscarEpisodioPorSerie() {
        listarSeriesBuscadas();
        System.out.println("Ingrese el nombre de la Serie para buscar sus episodios...");
        var nombreSerie = teclado.nextLine();

        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase()))
                .findFirst();

        if (serie.isPresent()) {
            var serieBuscada = serie.get();
            List<DatosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <serieBuscada.getTotalDeTemporadas() ; i++) {
                var json = consumoApi.obtenerDatos(URL + serieBuscada.getTitulo().replace(" ", "+") + "&Season=" + i + API_KEY);
                DatosTemporada datosTemporada = conversor.obtenerDatos(json, DatosTemporada.class);
                temporadas.add(datosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());

            serieBuscada.setEpisodios(episodios);
            repository.save(serieBuscada);
        }

    }

    private void listarSeriesBuscadas() {
        series = repository.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSeriesPorTitulo(){
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        serieBuscada = repository.findByTituloContainingIgnoreCase(nombreSerie);

        if(serieBuscada.isPresent()){
            System.out.println("La Serie que ha buscado es: " + serieBuscada.get());
        } else {
            System.out.println("La Serie no se ha encontrado");
        }

    }
    private void buscarTop5Series(){
        List<Serie> topSeries = repository.findTop5ByOrderByEvaluacionDesc();
        topSeries.forEach(s ->
                System.out.println("Nombre Serie: " + s.getTitulo() + " Evaluación: " + s.getEvaluacion()) );
    }

    private void buscarSeriesPorCategoria(){
        System.out.println("Escriba el genero/categoría de la serie que desea buscar");
        var genero = teclado.nextLine();
        var categoria = Categoria.fromEspanol(genero);
        List<Serie> seriesPorCategoria = repository.findByGenero(categoria);
        System.out.println("Las series de la categoría buscada son: " + genero);
        seriesPorCategoria.forEach(System.out::println);
    }
    public void filtrarSeriesPorTemporadaYEvaluacion(){
        System.out.println("¿Filtrar Series con cuántas Temporadas? ");
        var totalTemporadas = teclado.nextInt();
        teclado.nextLine();
        System.out.println("¿Y con Evaluación apartir de qué valor? ");
        var evaluacion = teclado.nextDouble();
        teclado.nextLine();
        List<Serie> filtroSeries = repository.seriesPorTemporadaYEvaluacion(totalTemporadas, evaluacion);
        System.out.println("*** Las Series filtradas y encontradas son ***");
        filtroSeries.forEach(s ->
                System.out.println(s.getTitulo() + "  - evaluación: " + s.getEvaluacion()));
    }

    private void  buscarEpisodiosPorTitulo(){
        System.out.println("Escribe el nombre del Episodio que deseas buscar");
        var nombreEpisodio = teclado.nextLine();
        List<Episodio> episodiosEncontrados = repository.episodiosPorNombre(nombreEpisodio);
        episodiosEncontrados.forEach(e ->
                System.out.printf("Serie: %s - Temporada %s - Episodio %s - Evaluación %s\n",
                        e.getSerie().getTitulo(), e.getTemporada(), e.getNumeroEpisodio(), e.getEvaluacion()));

    }

    private void buscarTop5Episodios(){
        buscarSeriesPorTitulo();
        if(serieBuscada.isPresent()){
            Serie serie = serieBuscada.get();
            List<Episodio> topEpisodios = repository.top5Episodios(serie);
            topEpisodios.forEach(e ->
                    System.out.printf("Serie: %s - Temporada %s - Episodio %s - Evaluación %s\n",
                            e.getSerie().getTitulo(), e.getTemporada(), e.getTitulo(), e.getEvaluacion()));

        }
    }
}














