package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MonederoTest {
  private Cuenta cuenta;

  @BeforeEach
  void init() {
// Al considerarse un monto inicial, lo mejor seria que arranque desde 0.
    cuenta = new Cuenta(0);
  }

  @Test
  void PonerSaldoNuevoEnLaCuenta() {
    cuenta.poner(1500);
// En este caso, agrego un assert para verificar que la funcion poner funciona bien
    assertEquals(cuenta.getSaldo(),1500);
  }

  @Test
  void PonerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.poner(-1500));
  }

  @Test
  void TresDepositosEstanPermitidos() {
    cuenta.poner(1500);
    cuenta.poner(456);
    cuenta.poner(1900);
    assertEquals(cuenta.getSaldo(),1500+456+1900);
// Al no tirar ninguna excepcion, esta bien agregar 3 depositos.
  }

  @Test
  void MasDeTresDepositos() {
    assertThrows(MaximaCantidadDepositosException.class, () -> {
      cuenta.poner(1500);
      cuenta.poner(456);
      cuenta.poner(1900);
      cuenta.poner(245);
    });
  }


  @Test
  void ExtraerMasQueElSaldo() {
    assertThrows(SaldoMenorException.class, () -> {
      //aca decidimos que lo indicado no era setear ya que sino no respetariamos lo del encapsulamiento,
      // ademas para agregar saldo, lo indicado seria agregarselo en el constructor o al realizar un deposito
      Cuenta nueva = cuentaAgregada(90);
      nueva.sacar(100);
    });
  }

  @Test
  public void ExtraerMasDe1000() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      Cuenta nueva = cuentaAgregada(5000);
      nueva.sacar(1001);
    });
  }

  @Test
  public void ExtraerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.sacar(-500));
  }

// con cuentaAgregada() se puede crear una cuenta nueva sin tener que andar setteando cada vez que corremos un test
  Cuenta cuentaAgregada(double saldo){
    return new Cuenta(saldo);
  }

}



