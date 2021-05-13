package dds.monedero.model;

import java.time.LocalDate;

public abstract class Movimiento {
  private LocalDate fecha;
  //En ningún lenguaje de programación usen jamás doubles para modelar dinero en el mundo real
  //siempre usen numeros de precision arbitraria, como BigDecimal en Java y similares
  private double monto;
  // private boolean esDeposito; // Algo que pense es que esDeposito, en vez de ser un booleando, podria comportarse como dos clases
  // que heredan de Movimiento que se llamen Deposito y Extraido y que tengan comportamiento (Primitive Obsession)

  // y hacer que hereden esta funcion
  public double calcularValor(Cuenta cuenta) {
    return cuenta.getSaldo() + this.getMonto();
  }

  public Movimiento(LocalDate fecha, double monto/*, boolean esDeposito*/) {
    this.fecha = fecha;
    this.monto = monto;
    //this.esDeposito = esDeposito;
  }

  public double getMonto() {
    return monto;
  }

  public LocalDate getFecha() {
    return fecha;
  }

  // En fueDepositado() y fueExtraido() se puede considerar como que hay un poco de repeticion de logica (duplicate code) porque
  // ambas abarcan la misma idea, lo unico que cambia es que en uno fue depositado y en el otro fue extraido. Capaz se podria considerar
  // hacer un metodo solo donde haya una condicion que diga que si esDeposito es true y es de una determinada fecha, entonces fueDepositado. Caso contrario fue extraido.
  public boolean fueDepositado(LocalDate fecha) {
    return isDeposito() && esDeLaFecha(fecha);
  }

  public boolean fueExtraido(LocalDate fecha) {
    return isExtraccion() && esDeLaFecha(fecha);
  }

  public boolean esDeLaFecha(LocalDate fecha) {
    return this.fecha.equals(fecha);
  }

  public boolean isDeposito() {
    return esDeposito;
  }

  public boolean isExtraccion() {
    return !esDeposito;
  }


  // En este caso hay Feature Envy porque este metodo podria hacer su comportamiento directamente en Cuenta.
  // Ademas hay repeticion de logica con la funcion agregarMovimiento()
  /* public void agregateA(Cuenta cuenta) {
    cuenta.setSaldo(calcularValor(cuenta));
    cuenta.agregarMovimiento(fecha, monto, esDeposito);
  }
*/


}

class Deposito extends Movimiento{
  public Deposito(LocalDate fecha, double monto){
    super(fecha, monto);
  }

  public double calcularValor(Cuenta cuenta){
  //TODO: agregar comportamiento
  }
}


class Extraccion extends Movimiento{
  public Extraccion(LocalDate fecha, double monto){
    super(fecha, monto);
  }

  public double calcularValor(Cuenta cuenta){
  // TODO: agregar comportamiento
  }
}