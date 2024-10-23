package com.ecotourexpress.ecotourexpress.service;

import com.ecotourexpress.ecotourexpress.model.Cliente;
import com.ecotourexpress.ecotourexpress.model.Hospedaje;
import com.ecotourexpress.ecotourexpress.model.Producto;
import com.ecotourexpress.ecotourexpress.config.exception.ResourceNotFoundException;
import com.ecotourexpress.ecotourexpress.model.Actividad;
import com.ecotourexpress.ecotourexpress.model.Ruta;
import com.ecotourexpress.ecotourexpress.repository.ClienteRepository;
import com.ecotourexpress.ecotourexpress.repository.HospedajeRepository;
import com.ecotourexpress.ecotourexpress.repository.ProductoRepository;
import com.ecotourexpress.ecotourexpress.repository.ActividadRepository;
import com.ecotourexpress.ecotourexpress.repository.RutaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    // Conexión a repositorios
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ActividadRepository actividadRepository;

    @Autowired
    private RutaRepository rutaRepository;

    @Autowired
    private HospedajeRepository hospedajeRepository;

    @Autowired
    private ProductoRepository productoRepository;

    // Guardar o actualizar cliente
    public Cliente saveCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    // Obtener todos los clientes
    public List<Cliente> getAllClientes() {
        List<Cliente> clientes = new ArrayList<>();
        clienteRepository.findAll().forEach(clientes::add);
        return clientes;
    }

    // Obtener cliente por ID (editar)
    public Optional<Cliente> getClienteById(int id) {
        return clienteRepository.findById(id);
    }

    // Eliminar cliente
    public void deleteCliente(int id) {
        clienteRepository.deleteById(id);
    }

    // Añadir actividades al cliente con verificación de capacidad
    public Cliente addActividadesToCliente(int id_cliente, List<Integer> actividadIds) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
    
        for (Integer actividadId : actividadIds) {
            Actividad existingActividad = actividadRepository.findById(actividadId)
                    .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada con id: " + actividadId));
    
            // Verificar que la actividad no esté ya asociada al cliente
            if (!cliente.getActividades().contains(existingActividad)) {
                cliente.getActividades().add(existingActividad);
                // Ajusta la capacidad si es necesario
                existingActividad.setCapacidad(existingActividad.getCapacidad() - 1);
                actividadRepository.save(existingActividad); // Guardar cambios en la actividad
            }
        }
    
        // Guardar cambios en el cliente
        return clienteRepository.save(cliente);
    }
    


    // Obtener actividades del cliente
    public List<Actividad> getActividadesOfCliente(int id_cliente) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
        return cliente.getActividades();
    }

    // Eliminar actividad del cliente y ajustar la capacidad
    public Cliente removeActividadFromCliente(int id_cliente, int id_actividad) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
        
        Actividad actividad = actividadRepository.findById(id_actividad)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada con id: " + id_actividad));
        
        // Verificar si la actividad pertenece al cliente antes de eliminarla
        if (cliente.getActividades().contains(actividad)) {
            // Incrementar la capacidad de la actividad
            actividad.setCapacidad(actividad.getCapacidad() + 1);
            actividadRepository.save(actividad); // Guardar cambios en la actividad

            // Eliminar la actividad del cliente
            cliente.getActividades().remove(actividad);
        } else {
            throw new RuntimeException("La actividad no está asignada a este cliente.");
        }

        return clienteRepository.save(cliente);
    }


    // Añadir rutas al cliente
    public Cliente addRutasToCliente(int id_cliente, List<Integer> rutaIds) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
        
        for (Integer id_ruta : rutaIds) {
            Ruta rut = rutaRepository.findById(id_ruta)
                    .orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrada con id: " + id_ruta));
            
            // Verificar la capacidad de las actividades
            boolean capacidadSuficiente = true;
            for (Actividad actividad : rut.getActividades()) {
                if (actividad.getCapacidad() <= 0) {
                    capacidadSuficiente = false;
                    break;
                }
            }
            
            if (capacidadSuficiente) {
                // Decrementar la capacidad de la ruta
                rut.setCapacidad(rut.getCapacidad() - 1);
                rutaRepository.save(rut); // Guardar cambios en la ruta
                
                // Decrementar la capacidad de cada actividad
                for (Actividad actividad : rut.getActividades()) {
                    actividad.setCapacidad(actividad.getCapacidad() - 1);
                    actividadRepository.save(actividad); // Guardar cambios en la actividad
                }

                cliente.getRutas().add(rut); // Agregar la ruta al cliente
            } else {
                throw new RuntimeException("No hay suficiente capacidad en las actividades de la ruta.");
            }
        }

        return clienteRepository.save(cliente);
    }
    

    // Obtener rutas del cliente
    public List<Ruta> getRutasOfCliente(int id_cliente) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
        return cliente.getRutas();
    }

    // Eliminar ruta del cliente y ajustar la capacidad
    public Cliente removeRutaFromCliente(int id_cliente, int id_ruta) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
        
        Ruta ruta = rutaRepository.findById(id_ruta)
                .orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrada con id: " + id_ruta));
        
        // Verificar si la ruta pertenece al cliente antes de eliminarla
        if (cliente.getRutas().contains(ruta)) {
            // Incrementar la capacidad de la ruta
            ruta.setCapacidad(ruta.getCapacidad() + 1);
            rutaRepository.save(ruta); // Guardar cambios en la ruta

            // Incrementar la capacidad de cada actividad
            for (Actividad actividad : ruta.getActividades()) {
                actividad.setCapacidad(actividad.getCapacidad() + 1);
                actividadRepository.save(actividad); // Guardar cambios en la actividad
            }

            // Eliminar la ruta del cliente
            cliente.getRutas().remove(ruta);
        } else {
            throw new RuntimeException("La ruta no está asignada a este cliente.");
        }

        return clienteRepository.save(cliente);
    }

    // Consultar hospedaje de cliente
    public Hospedaje getHospedajeOfCliente(int id_cliente) {
    Cliente cliente = clienteRepository.findById(id_cliente)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));

    return cliente.getHabitacion();
    }

    // Asignar hospedaje a Cliente
    public Cliente addHospedajeToCliente(int id_cliente, int id_hospedaje) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
    
        // Buscar el hospedaje usando el ID
        Hospedaje hos = hospedajeRepository.findById(id_hospedaje)
                .orElseThrow(() -> new ResourceNotFoundException("Hospedaje no encontrado con id: " + id_hospedaje));
    
        // Verificar disponibilidad
        if (hos.getDisponibilidad() > 0) {
            cliente.setHabitacion(hos);
            hos.setDisponibilidad(hos.getDisponibilidad() - 1);
            hospedajeRepository.save(hos);
        } else {
            throw new IllegalStateException("No hay habitaciones disponibles para el hospedaje con id: " + id_hospedaje);
        }
    
        return clienteRepository.save(cliente);
    }    
    
    // Eliminar hospedaje de cliente
    public Cliente removeHospedajeFromCliente(int id_cliente) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
    
        Hospedaje hos = cliente.getHabitacion();
        if (hos != null) {
            hos.setDisponibilidad(hos.getDisponibilidad() + 1);
            hospedajeRepository.save(hos);
            cliente.setHabitacion(null);
        }
    
        return clienteRepository.save(cliente);
    }
    
    // Agregar productos al cliente y ajustar existencias
    public Cliente addProductosToCliente(int id_cliente, List<Integer> id_productos) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));

        for (int productoId : id_productos) {
            Producto producto = productoRepository.findById(productoId)
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + productoId));

            if (producto.getCantidad_disponible() > 0) {
                // Agregar el producto a la lista de productos del cliente
                cliente.getProductos().add(producto);
                
                // Decrementar la cantidad disponible del producto
                producto.setCantidad_disponible(producto.getCantidad_disponible() - 1);
                productoRepository.save(producto); // Guardar cambios en el producto
            } else {
                throw new IllegalStateException("No hay existencias disponibles para el producto con id: " + productoId);
            }
        }

        return clienteRepository.save(cliente);
    }
    

    // Consultar productos asignados a cliente
    public List<Producto> getProductoOfCliente(int id_cliente) {
        Cliente cliente = clienteRepository.findById(id_cliente)
        .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));

    return cliente.getProductos();
    }

    // Eliminar productos del cliente y ajustar existencias
    public Cliente removeProductosFromCliente(int id_cliente, List<Integer> id_productos) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));

        for (int id_producto : id_productos) {
            Producto producto = productoRepository.findById(id_producto)
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id_producto));

            // Verificar si el producto pertenece al cliente antes de eliminarlo
            if (cliente.getProductos().contains(producto)) {
                // Incrementar la cantidad disponible del producto
                producto.setCantidad_disponible(producto.getCantidad_disponible() + 1);
                productoRepository.save(producto); // Guardar cambios en el producto

                // Eliminar el producto de la lista de productos del cliente
                cliente.getProductos().remove(producto);
            } else {
                throw new RuntimeException("El producto con id: " + id_producto + " no está asignado a este cliente.");
            }
        }

        return clienteRepository.save(cliente);
    }

}
