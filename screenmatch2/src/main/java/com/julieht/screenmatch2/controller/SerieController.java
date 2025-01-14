package com.julieht.screenmatch2.controller;

import com.julieht.screenmatch2.dto.EpisodioDTO;
import com.julieht.screenmatch2.dto.SerieDTO;
import com.julieht.screenmatch2.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.julieht.screenmatch2.modelo.Categoria;
import com.julieht.screenmatch2.repository.ISerieRepository;
import java.util.stream.Collectors;
import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {

    @Autowired
    private SerieService servicio;

    @GetMapping()
    public List<SerieDTO> obtenerLasSeries() {
        return servicio.obtenerLasSeries();
    }

    @GetMapping("/top5")
    public List<SerieDTO> obtenerTop5(){
        return servicio.obtenerTop5();
    }

    @GetMapping("/lanzamientos")
    public List<SerieDTO> obtenerLanzamientosMasRecientes(){
        return servicio.obtenerLanzamientosMasRecientes();
    }

    @GetMapping("/{id}")
    public SerieDTO obtenerSeriePorId(@PathVariable Long id){
        return servicio.obtenerSeriePorId(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDTO> obtenerLasTemporadas(@PathVariable Long id) {
        return servicio.obtenerLasTemporadas(id);
    }

    @GetMapping("/{id}/temporadas/{numeroDeTemporada}")
    public List<EpisodioDTO> obtenerTemporadaPorNumero(@PathVariable Long id, @PathVariable Long numeroDeTemporada) {
        return servicio.obtenerTemporadasPorNumero(id, numeroDeTemporada);
    }

    @GetMapping("/{id}/temporadas/top")
    public List<EpisodioDTO> obterTopDeEpisodios(@PathVariable Long id){
        return servicio.obtenerTopDeEpisodios(id);
    }

    @GetMapping("/categoria/{nombreGenero}")
    public List<SerieDTO> obtenerSeriesPorCategoria(@PathVariable String nombreGenero) {
        return servicio.obtenerSeriesPorCategoria(nombreGenero);
    }
}
