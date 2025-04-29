package org.example.features.coupons;

import org.example.shared.SceneRouter;

public class CouponsController {
  private final CouponsRepository couponsRepository;
  private final SceneRouter sceneRouter;

  public CouponsController(CouponsRepository couponsRepository, SceneRouter sceneRouter) {
    this.couponsRepository = couponsRepository;
    this.sceneRouter = sceneRouter;
  }
}
